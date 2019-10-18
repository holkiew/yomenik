package com.holkiew.yomenik.battlesim.ship.battlesimulator;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.Army;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStage;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStrategy;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.dto.NewBattleRequest;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.model.BattleRecap;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.port.BattleHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.holkiew.yomenik.battlesim.common.util.EnumUtils.toEnumMap;


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

    public Flux<BattleHistory> getCurrentBattles(Principal principal) {
        return repository.findAllByUserIdAndIsIssuedFalseOrderByStartDate(principal.getId())
                .map(this::filterOutFutureResults);
    }

    public Mono<BattleHistory> cancelCurrentBattle(Principal principal) {
        return repository.findFirstByUserIdAndIsIssuedFalseOrderByStartDate(principal.getId())
                .map(battleHistory -> Tuples.of(battleHistory, getFirstNotIssuedStage(battleHistory)))
                .map(tuple -> updateCancelledBattleHistory(tuple.getT1(), tuple.getT2()))
                .flatMap(repository::save);
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

    private BattleRecap getFirstNotIssuedStage(BattleHistory battleHistory) {
        var nextStage = battleHistory.getCurrentStage().nextStage();
        return battleHistory.getBattleRecapMap().getOrDefault(nextStage, battleHistory.getBattleRecapMap().get(BattleStage.END));
    }

    private BattleHistory updateCancelledBattleHistory(BattleHistory battleHistory, BattleRecap firstNotIssuedBattleRecap) {
        var unresolvedRecapMap = battleHistory.getBattleRecapMap().entrySet().stream()
                .filter(entry -> entry.getKey().getValue() <= firstNotIssuedBattleRecap.getBattleStage().getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        battleHistory.setBattleRecapMap(unresolvedRecapMap);
        resolveRetreatingBattle(battleHistory, firstNotIssuedBattleRecap);
        return battleHistory;
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

    private BattleHistory filterOutFutureResults(BattleHistory battleHistory) {
        var filteredEnumMap = battleHistory.getBattleRecapMap()
                .entrySet().stream()
                .filter(entry -> entry.getValue().getIsIssued())
                .collect(toEnumMap(BattleStage.class));
        battleHistory.setBattleRecapMap(filteredEnumMap);
        return battleHistory;
    }


}
