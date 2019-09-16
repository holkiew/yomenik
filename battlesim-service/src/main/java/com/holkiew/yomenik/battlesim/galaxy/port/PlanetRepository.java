package com.holkiew.yomenik.battlesim.galaxy.port;

import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface PlanetRepository extends ReactiveMongoRepository<Planet, String> {
    Mono<Planet> findByIdAndUserId(String id, String userId);
}
