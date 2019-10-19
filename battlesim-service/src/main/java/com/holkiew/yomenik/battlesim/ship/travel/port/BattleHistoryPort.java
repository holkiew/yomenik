package com.holkiew.yomenik.battlesim.ship.travel.port;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.dto.NewBattleRequest;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import reactor.core.publisher.Mono;

public interface BattleHistoryPort {
    Mono<BattleHistory> newBattle(NewBattleRequest request);

    Mono<BattleHistory> findById(String id);
}
