package com.holkiew.yomenik.battlesim.ship.battlesimulator.port;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Repository
public interface BattleHistoryRepository extends ReactiveMongoRepository<BattleHistory, String> {

    Flux<BattleHistory> findAllByInvolvedUserIdsContainsAndIsIssuedFalseOrderByStartDate(String userId);

    Mono<BattleHistory> findFirstByInvolvedUserIdsContainsAndIsIssuedFalseOrderByStartDate(String userId);

    Flux<BattleHistory> findByInvolvedUserIdsContainsAndIsIssuedTrue(String userId, Pageable pageable);

    Mono<Long> countByInvolvedUserIdsContainsAndIsIssuedTrue(String userId);

    Flux<BattleHistory> findAllByIsIssuedFalseAndNextRoundDateBefore(LocalDateTime before);


}
