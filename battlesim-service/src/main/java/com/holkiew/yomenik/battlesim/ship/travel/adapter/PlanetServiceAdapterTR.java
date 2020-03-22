package com.holkiew.yomenik.battlesim.ship.travel.adapter;

import com.holkiew.yomenik.battlesim.planet.PlanetFacade;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.travel.port.PlanetPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PlanetServiceAdapterTR implements PlanetPort {

    private final PlanetFacade planetFacade;

    public Mono<Planet> findByIdAndUserId(String id, String userId) {
        return planetFacade.findByIdAndUserId(id, userId);
    }

    public Mono<Planet> findById(String id) {
        return planetFacade.findById(id);
    }

    public Flux<Planet> saveAll(Flux<Planet> planets) {
        return planetFacade.saveAll(planets);
    }
}
