package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.port.PlanetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlanetFacade {
    private final PlanetRepository planetRepository;

    public Mono<Planet> findByIdAndUserId(String id, String userId) {
        return planetRepository.findByIdAndUserId(id, userId);
    }

    public Mono<Planet> findById(String id) {
        return planetRepository.findById(id);
    }

    public Flux<Planet> saveAll(Flux<Planet> planets) {
        return planetRepository.saveAll(planets);
    }

}
