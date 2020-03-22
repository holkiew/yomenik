package com.holkiew.yomenik.battlesim.ship.fleetmanagement.adapter;

import com.holkiew.yomenik.battlesim.planet.PlanetFacade;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.port.PlanetPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PlanetServiceAdapterFM implements PlanetPort {
    private final PlanetFacade planetFacade;

    @Override
    public Flux<Planet> findAllByUserId(String userId) {
        return planetFacade.findAllByUserId(userId);
    }
}

