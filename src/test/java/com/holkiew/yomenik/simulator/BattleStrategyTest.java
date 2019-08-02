package com.holkiew.yomenik.simulator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.simulator.ships.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BattleStrategyTest {

    @Test
    public void simulateNextStageTest() {
        // Given
        int healthPoints1 = 100, damage1 = 25, healthPoints2 = 200, damage2 = 50, healthPoints3 = 500, damage3 = 100;
        int a1nShipsl1 = 10, a1nShipsl2 = 10, a2nShipsl1 = 10, a2nShipsl3 = 1;
        int a1HPSum = a1nShipsl1 * healthPoints1 + a1nShipsl2 * healthPoints2;
        int a2HPSum = a2nShipsl1 * healthPoints1 + a2nShipsl3 * healthPoints3;
        int a1DmgSum = a1nShipsl1 * damage1 + a1nShipsl2 * damage2;
        int a2DmgSum = a2nShipsl1 * damage1 + a2nShipsl3 * damage3;

        var a1Shipsl1 = Stream.generate(() -> new ShipLevel1(healthPoints1, damage1))
                .limit(a1nShipsl1).collect(Collectors.toCollection(ArrayList::new));
        var a1Shipsl2 = Stream.generate(() -> new ShipLevel2(healthPoints2, damage2))
                .limit(a1nShipsl2).collect(Collectors.toCollection(ArrayList::new));
        var a2Shipsl1 = Stream.generate(() -> new ShipLevel1(healthPoints1, damage1))
                .limit(a2nShipsl1).collect(Collectors.toCollection(ArrayList::new));
        var a2Shipsl3 = Stream.generate(() -> new ShipLevel3(healthPoints3, damage3))
                .limit(a2nShipsl3).collect(Collectors.toCollection(ArrayList::new));

        ListMultimap<ShipName, Ship> army1Map = ArrayListMultimap.create(), army2Map = ArrayListMultimap.create();
        army1Map.putAll(ShipName.SHIP_LEVEL1, a1Shipsl1);
        army1Map.putAll(ShipName.SHIP_LEVEL2, a1Shipsl2);
        army2Map.putAll(ShipName.SHIP_LEVEL1, a2Shipsl1);
        army2Map.putAll(ShipName.SHIP_LEVEL3, a2Shipsl3);
        Army army1 = new Army(army1Map), army2 = new Army(army2Map);

        BattleStrategy battleStrategy = BattleStrategy.of(army1, army2);

        // When
        IntStream.range(0, 2)
                .forEach(i -> {
                    assertEquals(i, battleStrategy.getBattleStage().getValue());
                    battleStrategy.resolveRound();
                    if (i == 0) {
                        var a1CurrentHPSumList = new ArrayList<>(army1.getShips().values());
                        a1CurrentHPSumList.addAll(army1.getDestroyedShips().values());
                        var a1CurrentHPSum = a1CurrentHPSumList.stream().mapToInt(value -> Math.abs(value.getHealthPoints())).sum();
                        assertEquals(a1CurrentHPSum, a1HPSum - (i + 1) * a2DmgSum);
                    }
                });

        while (battleStrategy.getBattleStage() != BattleStage.END) {
            battleStrategy.resolveRound();
        }

        // Then
        assertEquals(BattleStage.END, battleStrategy.getBattleStage());
        assertEquals(0, army2.getShips().size());
        assertEquals(a2nShipsl1 + a2nShipsl3, army2.getDestroyedShips().size());
    }
}