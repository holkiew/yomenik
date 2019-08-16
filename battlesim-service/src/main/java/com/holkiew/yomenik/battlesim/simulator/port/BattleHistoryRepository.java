package com.holkiew.yomenik.battlesim.simulator.port;

import com.holkiew.yomenik.battlesim.simulator.entity.BattleHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface BattleHistoryRepository extends ReactiveMongoRepository<BattleHistory, String> {

    Mono<BattleHistory> findFirstByIsIssuedFalseOrderByStartDate();

    Flux<BattleHistory> findFirstByIsIssuedTrue(Pageable pageable);

}
