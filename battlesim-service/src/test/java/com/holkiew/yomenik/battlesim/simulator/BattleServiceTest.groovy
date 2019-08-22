package com.holkiew.yomenik.battlesim.simulator

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal
import com.holkiew.yomenik.battlesim.simulator.entity.BattleHistory
import com.holkiew.yomenik.battlesim.simulator.port.BattleHistoryRepository
import com.holkiew.yomenik.battlesim.simulator.ship.battle.Army
import com.holkiew.yomenik.battlesim.simulator.ship.battle.BattleStage
import com.holkiew.yomenik.battlesim.simulator.ship.battle.BattleStrategy
import com.holkiew.yomenik.battlesim.simulator.ship.type.ShipName
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.LocalDateTime

import static org.assertj.core.api.Assertions.assertThat

class BattleServiceTest extends Specification {

    def repository = Mock(BattleHistoryRepository)
    BattleService testedObj
    Principal principal

    def setup() {
        testedObj = Spy(new BattleService(this.repository))
        principal = new Principal("id", "username")
    }

    def "Should cancel unissued battle"() {
        given: "STAGE.NEW is only issued"
            def army1Map = [(ShipName.SHIP_LEVEL1): 23L, (ShipName.SHIP_LEVEL2): 2L]
            def army2Map = [(ShipName.SHIP_LEVEL1): 23L, (ShipName.SHIP_LEVEL3): 2L]
            def army1 = Army.of(army1Map), army2 = Army.of(army2Map)
            def battleStrategy = BattleStrategy.of(army1, army2)
            battleStrategy.battleStage = BattleStage.NEW
            def battleHistory = new BattleHistory(battleStrategy, principal.getId(), LocalDateTime.now().minusSeconds(3), 3)
            battleStrategy.battleStage = BattleStage.ROUND_1
            battleHistory.addNewEntry(battleStrategy, LocalDateTime.now().plusSeconds(10))
            battleStrategy.battleStage = BattleStage.ROUND_2
            battleHistory.addNewEntry(battleStrategy, LocalDateTime.now().plusSeconds(11))
        and:
            1 * repository.findFirstByUserIdAndIsIssuedFalseOrderByStartDate(principal.getId()) >> Mono.just(battleHistory)
            1 * repository.save(_ as BattleHistory) >> { BattleHistory bh -> Mono.just(bh) }
        when:
            def publisher = testedObj.cancelCurrentBattle(principal)
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
            def battleHistory = new BattleHistory(battleStrategy, principal.getId(), LocalDateTime.now().minusSeconds(3), 3)
            battleStrategy.battleStage = BattleStage.ROUND_1
            battleHistory.addNewEntry(battleStrategy, LocalDateTime.now().minusSeconds(3))
            battleStrategy.battleStage = BattleStage.END
            battleHistory.addNewEntry(battleStrategy, LocalDateTime.now().plusSeconds(3))
        and:
            1 * repository.findFirstByUserIdAndIsIssuedFalseOrderByStartDate(principal.getId()) >> Mono.just(battleHistory)
            0 * repository.save(_)
        when:
            def publisher = testedObj.cancelCurrentBattle(principal)
        then: "History should be filtered out"
            StepVerifier.create(publisher)
                    .expectSubscription()
                    .expectNextCount(0)
                    .verifyComplete()
    }
}
