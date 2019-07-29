package com.holkiew.yomenik.simulator.persistence;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BattleHistoryRepository extends ReactiveMongoRepository<BattleHistory, String> {


    Mono<BattleHistory> findFirstByIsIssuedFalseOrderByStartDate();
    Mono<BattleHistory> findFirstByIsIssuedTrueOrderByStartDateDesc();

}
