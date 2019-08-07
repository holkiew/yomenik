package com.holkiew.yomenik.simulator;

import com.holkiew.yomenik.simulator.dto.NewBattleRequest;
import com.holkiew.yomenik.simulator.persistence.BattleHistory;
import com.holkiew.yomenik.simulator.persistence.BattleHistoryRepository;
import com.holkiew.yomenik.simulator.persistence.BattleRecap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.rmi.activation.UnknownObjectException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.holkiew.yomenik.util.UtilMethods.toEnumMap;


@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleHistoryRepository repository;

    public Flux<BattleHistory> getBattleHistory(Pageable pageable) {
        return repository.findFirstByIsIssuedTrue(pageable);
    }

    public void newBattle(NewBattleRequest request) {
        var army1 = Army.of(request.getArmy1());
        var army2 = Army.of(request.getArmy2());
        resolveBattle(army1, army2);
    }

    public Mono<BattleHistory> getCurrentBattle() {
        return repository.findFirstByIsIssuedFalseOrderByStartDate()
                .handle(updateIssuedBattles())
                .retry(UnknownObjectException.class::isInstance)
                .cast(BattleHistory.class)
//                .switchIfEmpty(repository.findFirstByIsIssuedTrueOrderByStartDateDesc())
                .map(filterOutFutureResults());
    }

    public Mono<BattleHistory> cancelCurrentBattle() {
        return repository.findFirstByIsIssuedFalseOrderByStartDate()
                .handle((battleHistory, synchronousSink) -> {
                    Optional<Map.Entry<BattleStage, BattleRecap>> notIssuedNextStage = battleHistory.getBattleRecapMap().entrySet()
                            .stream()
                            .filter(entry -> entry.getKey() != BattleStage.END && entry.getValue().getIsNotIssued())
                            .reduce((e1, e2) -> e1.getKey().getValue() < e2.getKey().getValue() ? e1 : e2);
                    if (notIssuedNextStage.isPresent()) {
                        updateBattleHIstory(battleHistory, notIssuedNextStage);
                        repository.save(battleHistory).subscribe();
                        synchronousSink.complete();
                    } else {
                        synchronousSink.next(Mono.empty());
                    }
                }).cast(BattleHistory.class);
    }

    private void updateBattleHIstory(BattleHistory battleHistory, Optional<Map.Entry<BattleStage, BattleRecap>> notIssuedNextStage) {
        var notIssuedNextStageEntry = notIssuedNextStage.get();
        var unresolvedRecapMap = battleHistory.getBattleRecapMap().entrySet().stream()
                .filter(entry -> entry.getKey().getValue() <= notIssuedNextStageEntry.getKey().getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        battleHistory.setBattleRecapMap(unresolvedRecapMap);
        resolveRetreatingBattle(battleHistory, notIssuedNextStageEntry.getValue());
    }

    private void resolveBattle(Army army1, Army army2) {
        long stageDelay = 3;
        BattleStrategy battleStrategy = BattleStrategy.of(army1, army2);
        LocalDateTime time = LocalDateTime.now();
        var battleHistory = new BattleHistory(battleStrategy, time);
        while (!battleStrategy.isBattleEnded()) {
            battleStrategy.resolveRound();
            time = time.plusSeconds(stageDelay);
            battleHistory.addNewEntry(battleStrategy, time);
        }
        battleHistory.setEndDate(time.plusSeconds(stageDelay));
        repository.save(battleHistory).doOnError(Throwable::printStackTrace).subscribe();
    }

    private void resolveRetreatingBattle(BattleHistory battleHistory, BattleRecap battleRecap) {
        long stageDelay = 3;
        var retreatingArmy = Army.of(battleRecap.getArmy1Recap());
        var army2 = Army.of(battleRecap.getArmy1Recap());

        BattleStrategy battleStrategy = BattleStrategy.of(retreatingArmy, army2);
        LocalDateTime time = LocalDateTime.now().plusSeconds(stageDelay);
        battleStrategy.resolveRetreatRound();
        battleHistory.addNewEntry(battleStrategy, time);
        battleHistory.setEndDate(time.plusSeconds(stageDelay));
    }

    private BiConsumer<BattleHistory, SynchronousSink<Object>> updateIssuedBattles() {
        return (battleHistory, synchronousSink) -> {
            var currentTime = LocalDateTime.now();
            var isIssued = battleHistory.getEndDate().isBefore(currentTime.minusSeconds(5));
            battleHistory.setIsIssued(isIssued);
            if (isIssued) {
                // TODO customowy ex
                repository.save(battleHistory).subscribe();
                synchronousSink.error(new UnknownObjectException("Object has been issued"));
            } else {
                battleHistory.getBattleRecapMap().values().stream()
                        .filter(BattleRecap::getIsNotIssued)
                        .forEach(battleRecap -> battleRecap.setIsIssued(battleRecap.getIssueTime().isBefore(currentTime)));
                repository.save(battleHistory).subscribe();
                synchronousSink.next(battleHistory);
            }
        };
    }

    private Function<BattleHistory, BattleHistory> filterOutFutureResults() {
        return battleHistory -> {
            if (!battleHistory.getIsIssued()) {
                var filteredEnumMap = battleHistory.getBattleRecapMap()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().getIsIssued())
                        .collect(toEnumMap(BattleStage.class));
                battleHistory.setBattleRecapMap(filteredEnumMap);
            }
            return battleHistory;
        };
    }

}
