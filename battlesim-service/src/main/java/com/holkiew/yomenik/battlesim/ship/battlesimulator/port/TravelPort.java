package com.holkiew.yomenik.battlesim.ship.battlesimulator.port;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import reactor.core.publisher.Mono;

public interface TravelPort {
    Mono<Boolean> battleEndDateChangeEvent(BattleHistory battleHistory);
}
