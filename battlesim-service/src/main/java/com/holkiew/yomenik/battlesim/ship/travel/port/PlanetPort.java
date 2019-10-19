package com.holkiew.yomenik.battlesim.ship.travel.port;

import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlanetPort {
    Mono<Planet> findByIdAndUserId(String id, String userId);

    Mono<Planet> findById(String id);

    Flux<Planet> saveAll(Flux<Planet> planets);
}
