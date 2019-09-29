package com.holkiew.yomenik.battlesim.ship.battlesimulator;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.Army;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStage;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStrategy;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.dto.NewBattleRequest;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.model.BattleRecap;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.model.exception.HistoryAlreadyIssuedException;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.port.BattleHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.holkiew.yomenik.battlesim.ship.util.UtilMethods.toEnumMap;


@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleHistoryRepository repository;

    public Mono<Tuple2<List<BattleHistory>, Long>> getBattleHistory(Principal principal, Pageable pageable) {
        // TODO Trzeba jednym customowym zapytaniem to zrobic
        return repository.findByUserIdAndIsIssuedTrue(principal.getId(), pageable)
                .collectList()
                .zipWith(repository.countByUserIdAndIsIssuedTrue(principal.getId()));
    }

    public Mono<BattleHistory> newBattle(NewBattleRequest request, Principal principal) {
        var army1 = Army.of(request.getArmy1());
        var army2 = Army.of(request.getArmy2());
        return resolveBattle(army1, army2, request.getStageDelay(), principal.getId());
    }

    public Mono<BattleHistory> getCurrentBattle(Principal principal) {
        return repository.findFirstByUserIdAndIsIssuedFalseOrderByStartDate(principal.getId())
                .handle(updateIssuedBattles())
                .retry(HistoryAlreadyIssuedException.class::isInstance)
                .cast(BattleHistory.class)
                .flatMap(repository::save)
                .map(filterOutFutureResults());

    }

    public Mono<BattleHistory> cancelCurrentBattle(Principal principal) {
        return repository.findFirstByUserIdAndIsIssuedFalseOrderByStartDate(principal.getId())
                .flatMap(battleHistory -> getFirstNotIssuedStage(battleHistory)
                        .map(entry -> Mono.just(Tuples.of(battleHistory, entry)))
                        .orElse(Mono.empty()))
                .map(tuple -> updateCancelledBattleHistory(tuple.getT1(), tuple.getT2()))
                .flatMap(repository::save);
    }

    private Optional<Map.Entry<BattleStage, BattleRecap>> getFirstNotIssuedStage(BattleHistory battleHistory) {
        return battleHistory.getBattleRecapMap().entrySet()
                .stream()
                .filter(entry -> entry.getKey() != BattleStage.END && entry.getValue().getIsNotIssued())
                .reduce((e1, e2) -> e1.getKey().getValue() < e2.getKey().getValue() ? e1 : e2);
    }

    private BattleHistory updateCancelledBattleHistory(BattleHistory battleHistory, Map.Entry<BattleStage, BattleRecap> notIssuedNextStageEntry) {
        var unresolvedRecapMap = battleHistory.getBattleRecapMap().entrySet().stream()
                .filter(entry -> entry.getKey().getValue() <= notIssuedNextStageEntry.getKey().getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        battleHistory.setBattleRecapMap(unresolvedRecapMap);
        resolveRetreatingBattle(battleHistory, notIssuedNextStageEntry.getValue());
        return battleHistory;
    }

    protected Mono<BattleHistory> resolveBattle(Army army1, Army army2, long stageDelay, String userId) {
        var battleStrategy = BattleStrategy.of(army1, army2);
        var time = LocalDateTime.now();
        var battleHistory = new BattleHistory(battleStrategy, userId, time, stageDelay);
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
            var isIssued = battleHistory.getEndDate().isBefore(currentTime.minusSeconds(battleHistory.getStageDelay()));
            battleHistory.setIsIssued(isIssued);
            if (isIssued) {
                // TODO przegrzmocic metode, wywalic handle, nie powinno sie zagniezdzac publisherow
                repository.save(battleHistory).subscribe();
                synchronousSink.error(new HistoryAlreadyIssuedException("Object has been issued"));
            } else {
                // TODO to tez juz nie ma sensu, jest getter dynamicznie robiajacy obczajke
                battleHistory.getBattleRecapMap().values().stream()
                        .filter(BattleRecap::getIsNotIssued)
                        .forEach(battleRecap -> battleRecap.setIsIssued(battleRecap.getIssueTime().isBefore(currentTime)));
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
