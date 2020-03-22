package com.holkiew.yomenik.battlesim.ship.fleetmanagement.port;

import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import reactor.core.publisher.Flux;

public interface PlanetPort {
    Flux<Planet> findAllByUserId(String userId);
}
