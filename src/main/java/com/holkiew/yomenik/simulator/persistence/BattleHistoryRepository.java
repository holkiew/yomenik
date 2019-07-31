package com.holkiew.yomenik.simulator.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface BattleHistoryRepository extends ReactiveMongoRepository<BattleHistory, String> {

    Mono<BattleHistory> findFirstByIsIssuedFalseOrderByStartDate();

    Flux<BattleHistory> findFirstByIsIssuedTrue(Sort sort, Pageable pageable);

}
