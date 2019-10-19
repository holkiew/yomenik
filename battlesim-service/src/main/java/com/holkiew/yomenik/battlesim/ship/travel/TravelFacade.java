package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TravelFacade {
    private final TravelService travelService;

    public Mono<Boolean> battleEndDateChangeEvent(BattleHistory battleHistory) {
        return travelService.battleEndDateChangeEvent(battleHistory).hasElement();
    }

}
