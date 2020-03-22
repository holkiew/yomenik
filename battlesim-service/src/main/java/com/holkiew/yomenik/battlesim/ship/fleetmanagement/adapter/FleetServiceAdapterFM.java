package com.holkiew.yomenik.battlesim.ship.fleetmanagement.adapter;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.port.FleetPort;
import com.holkiew.yomenik.battlesim.ship.travel.TravelFacade;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class FleetServiceAdapterFM implements FleetPort {
    private final TravelFacade travelFacade;

    @Override
    public Flux<Fleet> findByIds(Iterable<String> ids) {
        return travelFacade.findByIds(ids);
    }
}
