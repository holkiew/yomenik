package com.holkiew.yomenik.battlesim.ship.battlesimulator.adapter;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.port.TravelPort;
import com.holkiew.yomenik.battlesim.ship.travel.TravelFacade;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TravelServiceAdapter implements TravelPort {
    TravelFacade travelFacade;

    public Mono<Boolean> battleEndDateChangeEvent(BattleHistory battleHistory) {
        return travelFacade.battleEndDateChangeEvent(battleHistory);
    }
}
