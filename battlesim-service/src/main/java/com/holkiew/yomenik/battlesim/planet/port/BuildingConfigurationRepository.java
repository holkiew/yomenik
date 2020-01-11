package com.holkiew.yomenik.battlesim.planet.port;

import com.holkiew.yomenik.battlesim.planet.entity.BuildingConfiguration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface BuildingConfigurationRepository extends ReactiveMongoRepository<BuildingConfiguration, String> {
    Mono<BuildingConfiguration> findFirstByOrderByCreationDate();
}
