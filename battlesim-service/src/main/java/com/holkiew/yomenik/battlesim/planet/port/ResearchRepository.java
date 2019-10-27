package com.holkiew.yomenik.battlesim.planet.port;

import com.holkiew.yomenik.battlesim.planet.entity.Research;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchRepository extends ReactiveMongoRepository<Research, String> {
}
