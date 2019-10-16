package com.holkiew.yomenik.battlesim.galaxy.port;

import com.holkiew.yomenik.battlesim.galaxy.entity.SolarSystem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolarSystemRepository extends ReactiveMongoRepository<SolarSystem, String> {

}