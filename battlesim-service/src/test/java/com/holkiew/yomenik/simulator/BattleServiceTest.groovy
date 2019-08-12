package com.holkiew.yomenik.simulator

import com.holkiew.yomenik.simulator.entity.BattleHistory
import com.holkiew.yomenik.simulator.port.BattleHistoryRepository
import com.holkiew.yomenik.simulator.ship.battle.Army
import com.holkiew.yomenik.simulator.ship.battle.BattleStage
import com.holkiew.yomenik.simulator.ship.battle.BattleStrategy
import com.holkiew.yomenik.simulator.ship.type.ShipName
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.LocalDateTime

import static org.assertj.core.api.Assertions.assertThat;

class BattleServiceTest extends Specification {

    def repository = Mock(BattleHistoryRepository)
    BattleService testedObj

    def setup() {
        testedObj = new BattleService(this.repository)
    }

    def "Should cancel unissued battle"() {
        given: "STAGE.NEW is only issued"
            def army1Map = [(ShipName.SHIP_LEVEL1): 23L, (ShipName.SHIP_LEVEL2): 2L]
            def army2Map = [(ShipName.SHIP_LEVEL1): 23L, (ShipName.SHIP_LEVEL3): 2L]
            def army1 = Army.of(army1Map), army2 = Army.of(army2Map)
            def battleStrategy = BattleStrategy.of(army1, army2)
            battleStrategy.battleStage = BattleStage.NEW
            def battleHistory = new BattleHistory(battleStrategy, LocalDateTime.now().minusSeconds(3), 3)
            battleStrategy.battleStage = BattleStage.ROUND_1
            battleHistory.addNewEntry(battleStrategy, LocalDateTime.now().plusSeconds(10))
            battleStrategy.battleStage = BattleStage.ROUND_2
            battleHistory.addNewEntry(battleStrategy, LocalDateTime.now().plusSeconds(11))
        and:
            1 * repository.findFirstByIsIssuedFalseOrderByStartDate() >> Mono.just(battleHistory)
            1 * repository.save(_ as BattleHistory) >> { BattleHistory bh -> Mono.just(bh) }
        when:
            def publisher = testedObj.cancelCurrentBattle()
        then: "first unissued should only remain and penalty round calculated"
            StepVerifier.create(publisher)
                    .expectSubscription()
                    .consumeNextWith({ next ->
                        assertThat(next.battleRecapMap.get(BattleStage.ROUND_2)).isNull()
                        assertThat(next.battleRecapMap.get(BattleStage.END)).isNotNull()
                        assertThat(next.battleRecapMap.size() == 3)
                    }).verifyComplete()
    }

    def "Shouldn't cancel unissued battle with only STAGE.END remaining"() {
        given: "STAGE.NEW is only issued"
            def army1Map = [(ShipName.SHIP_LEVEL1): 100L, (ShipName.SHIP_LEVEL2): 2L]
            def army2Map = [(ShipName.SHIP_LEVEL1): 1L, (ShipName.SHIP_LEVEL3): 1L]
            def army1 = Army.of(army1Map), army2 = Army.of(army2Map)
            def battleStrategy = BattleStrategy.of(army1, army2)
            battleStrategy.battleStage = BattleStage.NEW
            def battleHistory = new BattleHistory(battleStrategy, LocalDateTime.now().minusSeconds(3), 3)
            battleStrategy.battleStage = BattleStage.ROUND_1
            battleHistory.addNewEntry(battleStrategy, LocalDateTime.now().minusSeconds(3))
            battleStrategy.battleStage = BattleStage.END
            battleHistory.addNewEntry(battleStrategy, LocalDateTime.now().plusSeconds(3))
        and:
            1 * repository.findFirstByIsIssuedFalseOrderByStartDate() >> Mono.just(battleHistory)
            0 * repository.save(_)
        when:
            def publisher = testedObj.cancelCurrentBattle()
        then: "History should be filtered out"
            StepVerifier.create(publisher)
                    .expectSubscription()
                    .expectNextCount(0)
                    .verifyComplete()
    }
}
