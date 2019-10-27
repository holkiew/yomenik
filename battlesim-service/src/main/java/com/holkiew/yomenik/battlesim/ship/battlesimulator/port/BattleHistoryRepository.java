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

    Flux<BattleHistory> findAllByArmy1UserIdOrArmy2UserIdAndIsIssuedFalseOrderByStartDate(String army1UserId, String army2UserId);

    Mono<BattleHistory> findFirstByArmy1UserIdOrArmy2UserIdAndIsIssuedFalseOrderByStartDate(String army1UserId, String army2UserId);

    Flux<BattleHistory> findByArmy1UserIdOrArmy2UserIdAndIsIssuedTrue(String army1UserId, String army2UserId, Pageable pageable);

    Mono<Long> countByArmy1UserIdOrArmy2UserIdAndIsIssuedTrue(String army1UserId, String army2UserId);

    Flux<BattleHistory> findAllByIsIssuedFalseAndNextRoundDateBefore(LocalDateTime before);


}
