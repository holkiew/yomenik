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

    public Mono<BattleHistory> newBattle(NewBattleRequest request) {
        var army1 = Army.of(request.getArmy1());
        var army2 = Army.of(request.getArmy2());
        return resolveBattle(army1, army2, request.getStageDelay());
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
                .filter(battleHistory -> getFirstNotIssuedStage(battleHistory).isPresent())
                .map(battleHistory -> {
                    updateBattleHistory(battleHistory, getFirstNotIssuedStage(battleHistory).get());
                    return battleHistory;
                }).flatMap(repository::save);
    }

    private Optional<Map.Entry<BattleStage, BattleRecap>> getFirstNotIssuedStage(BattleHistory battleHistory) {
        Optional<Map.Entry<BattleStage, BattleRecap>> reduce = battleHistory.getBattleRecapMap().entrySet()
                .stream()
                .filter(entry -> entry.getKey() != BattleStage.END && entry.getValue().getIsNotIssued())
                .reduce((e1, e2) -> e1.getKey().getValue() < e2.getKey().getValue() ? e1 : e2);
        return reduce;
    }

    private void updateBattleHistory(BattleHistory battleHistory, Map.Entry<BattleStage, BattleRecap> notIssuedNextStageEntry) {
        var unresolvedRecapMap = battleHistory.getBattleRecapMap().entrySet().stream()
                .filter(entry -> entry.getKey().getValue() <= notIssuedNextStageEntry.getKey().getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        battleHistory.setBattleRecapMap(unresolvedRecapMap);
        resolveRetreatingBattle(battleHistory, notIssuedNextStageEntry.getValue());
    }

    protected Mono<BattleHistory> resolveBattle(Army army1, Army army2, long stageDelay) {
        var battleStrategy = BattleStrategy.of(army1, army2);
        var time = LocalDateTime.now();
        var battleHistory = new BattleHistory(battleStrategy, time, stageDelay);
        while (!battleStrategy.isBattleEnded()) {
            battleStrategy.resolveRound();
            time = time.plusSeconds(stageDelay);
            battleHistory.addNewEntry(battleStrategy, time);
        }
        battleHistory.setEndDate(time.plusSeconds(stageDelay));
        return repository.save(battleHistory);
    }

    private void resolveRetreatingBattle(BattleHistory battleHistory, BattleRecap battleRecap) {
        var retreatingArmy = Army.of(battleRecap.getArmy1Recap());
        var army2 = Army.of(battleRecap.getArmy1Recap());

        BattleStrategy battleStrategy = BattleStrategy.of(retreatingArmy, army2);
        LocalDateTime time = LocalDateTime.now().plusSeconds(battleHistory.getStageDelay());
        battleStrategy.resolveRetreatRound();
        battleHistory.addNewEntry(battleStrategy, time);
        battleHistory.setEndDate(time.plusSeconds(battleHistory.getStageDelay()));
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
