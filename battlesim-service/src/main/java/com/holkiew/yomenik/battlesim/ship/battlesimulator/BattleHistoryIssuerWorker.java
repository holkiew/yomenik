package com.holkiew.yomenik.battlesim.ship.battlesimulator;

import com.holkiew.yomenik.battlesim.common.ReactorWorker;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStage;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.model.BattleRecap;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.port.BattleHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;


@RequiredArgsConstructor
@Log4j2
@Component
public class BattleHistoryIssuerWorker implements ReactorWorker {

    private final BattleHistoryRepository repository;

    public Flux<?> getUpstream() {
        return Flux.interval(Duration.ofSeconds(1L))
                .flatMap(l -> repository.findAllByIsIssuedFalseAndNextRoundDateBefore(LocalDateTime.now()))
                .map(this::issuePartialBattles)
                .flatMap(repository::save);
    }

    @Override
    public Consumer<Throwable> doOnError() {
        return log::error;
    }

    @Override
    public Runnable doOnTerminate() {
        return () -> log.error("Worker " + this.getClass().getSimpleName() + " terminated");
    }

    @Override
    public Consumer<Subscription> doOnSubscribe() {
        return subscription -> log.info("Worker " + this.getClass().getSimpleName() + " initialized");
    }

    private BattleHistory issuePartialBattles(BattleHistory battleHistory) {
        Map<BattleStage, BattleRecap> battleHistoryBattleRecapMap = battleHistory.getBattleRecapMap();
        battleHistoryBattleRecapMap.get(battleHistory.getCurrentStage()).setIsIssued(true);
        BattleStage nextStage = battleHistory.getCurrentStage().nextStage();
        var nextBattleRecap = battleHistoryBattleRecapMap.get(nextStage);

        if (nextStage != BattleStage.END && Objects.isNull(nextBattleRecap)) {
            nextStage = BattleStage.END;
            nextBattleRecap = battleHistoryBattleRecapMap.get(BattleStage.END);
        }
        battleHistory.setCurrentStage(nextStage);
        battleHistory.setNextRoundDate(nextBattleRecap.getIssueTime());
        if (nextStage == BattleStage.END) {
            battleHistory.setIsIssued(true);
        }
        return battleHistory;
    }
}
