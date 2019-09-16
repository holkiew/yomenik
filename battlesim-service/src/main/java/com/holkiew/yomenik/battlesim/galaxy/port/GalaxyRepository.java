package com.holkiew.yomenik.battlesim.galaxy.port;

import com.holkiew.yomenik.battlesim.galaxy.entity.Galaxy;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GalaxyRepository extends ReactiveMongoRepository<Galaxy, Integer> {

}
