package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import com.holkiew.yomenik.battlesim.ship.travel.port.FleetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TravelFacade {
    private final TravelService travelService;
    private final FleetRepository fleetRepository;

    public Mono<Boolean> battleEndDateChangeEvent(BattleHistory battleHistory) {
        return travelService.battleEndDateChangeEvent(battleHistory).hasElement();
    }

    public Flux<Fleet> findByIds(Iterable<String> ids) {
        return fleetRepository.findAllById(ids);
    }

    public Flux<Fleet> findAllUnfinishedMissions() {
        return fleetRepository.findAllByArrivalTimeBeforeAndMissionCompletedFalse(LocalDateTime.now());
    }

}
