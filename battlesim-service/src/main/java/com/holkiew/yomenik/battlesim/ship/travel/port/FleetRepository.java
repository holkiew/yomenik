package com.holkiew.yomenik.battlesim.ship.travel.port;

import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface FleetRepository extends ReactiveMongoRepository<Fleet, String> {
    Flux<Fleet> findAllByArrivalTimeBeforeAndMissionCompletedFalse(LocalDateTime timeBefore);
}

