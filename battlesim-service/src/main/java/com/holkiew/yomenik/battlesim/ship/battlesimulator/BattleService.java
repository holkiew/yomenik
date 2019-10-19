package com.holkiew.yomenik.battlesim.ship.battlesimulator;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.Army;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStage;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStrategy;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.dto.NewBattleRequest;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.model.BattleRecap;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.port.BattleHistoryRepository;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.port.TravelPort;
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
    private final TravelPort travelService;
    private final long STAGE_DELAY = 5L;

    public Mono<Tuple2<List<BattleHistory>, Long>> getBattleHistory(Principal principal, Pageable pageable) {
        // TODO Trzeba jednym customowym zapytaniem to zrobic
        return repository.findByInvolvedUserIdsContainsAndIsIssuedTrue(principal.getId(), pageable)
                .collectList()
                .zipWith(repository.countByInvolvedUserIdsContainsAndIsIssuedTrue(principal.getId()));
    }

    public Mono<BattleHistory> newBattle(NewBattleRequest request) {
        var battleStrategy = BattleStrategy.of(Army.of(request.getArmy1()), Army.of(request.getArmy2()));
        var time = LocalDateTime.now();
        var battleHistory = new BattleHistory(battleStrategy, time, STAGE_DELAY, request.getArmy1UserId(), request.getArmy2UserId());
        while (!battleStrategy.isBattleEnded()) {
            battleStrategy.resolveRound();
            time = time.plusSeconds(STAGE_DELAY);
            battleHistory.addNewEntry(battleStrategy, time);
        }
        battleHistory.setEndDate(time.plusSeconds(STAGE_DELAY));
        return repository.save(battleHistory);
    }

    public Flux<BattleHistory> getCurrentBattles(Principal principal) {
        return repository.findAllByInvolvedUserIdsContainsAndIsIssuedFalseOrderByStartDate(principal.getId())
                .map(this::filterOutFutureResults);
    }

    public Mono<BattleHistory> cancelCurrentBattle(Principal principal) {
        return repository.findFirstByInvolvedUserIdsContainsAndIsIssuedFalseOrderByStartDate(principal.getId())
                .map(battleHistory -> Tuples.of(battleHistory, getFirstNotIssuedStage(battleHistory)))
                .map(tuple -> updateCancelledBattleHistory(tuple.getT1(), tuple.getT2()))
                .zipWhen(travelService::battleEndDateChangeEvent)
                .flatMap(tuple -> repository.save(tuple.getT1()));
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
        var army2 = Army.of(battleRecap.getArmy2Recap());

        BattleStrategy battleStrategy = BattleStrategy.of(retreatingArmy, army2);
        battleStrategy.resolveRetreatRound();
        battleHistory.addNewEntry(battleStrategy, battleHistory.getNextRoundDate());
        battleHistory.setEndDate(battleHistory.getNextRoundDate().plusSeconds(battleHistory.getStageDelay()));
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
