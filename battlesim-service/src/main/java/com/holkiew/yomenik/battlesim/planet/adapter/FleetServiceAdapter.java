package com.holkiew.yomenik.battlesim.planet.adapter;

import com.holkiew.yomenik.battlesim.planet.port.FleetPort;
import com.holkiew.yomenik.battlesim.ship.travel.TravelFacade;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class FleetServiceAdapter implements FleetPort {

    private final TravelFacade travelFacade;

    @Override
    public Flux<Fleet> findByIds(Iterable<String> ids) {
        return travelFacade.findByIds(ids);
    }
}