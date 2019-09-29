package com.holkiew.yomenik.battlesim.ship.battlesimulator.port;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface BattleHistoryRepository extends ReactiveMongoRepository<BattleHistory, String> {

    Mono<BattleHistory> findFirstByUserIdAndIsIssuedFalseOrderByStartDate(String userId);

    Flux<BattleHistory> findByUserIdAndIsIssuedTrue(String userId, Pageable pageable);

    Mono<Long> countByUserIdAndIsIssuedTrue(String userId);

}
