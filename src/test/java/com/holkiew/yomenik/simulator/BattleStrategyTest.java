package com.holkiew.yomenik.simulator;

import com.holkiew.yomenik.simulator.ships.Ship;
import com.holkiew.yomenik.simulator.ships.ShipLevel1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BattleStrategyTest {

    @Test
    public void simulateNextStageTest() {
        int healthPoints = 100, damage = 25;
        int a1nShips = 10, a2nShips = 5;
        var ships1 = Stream.generate(() -> (Ship)new ShipLevel1(healthPoints, damage))
                .limit(a1nShips).collect(Collectors.toCollection(ArrayList::new));
        var ships2 = Stream.generate(() -> (Ship)new ShipLevel1(healthPoints, damage))
                .limit(a2nShips).collect(Collectors.toCollection(ArrayList::new));

        Army army1 = new Army(ships1), army2 = new Army(ships2);
        BattleStrategy battleStrategy = BattleStrategy.of(army1, army2);

        IntStream.range(0, 2)
                .forEach(i -> {
                    assertEquals(i, battleStrategy.getBattleStage().getValue());
                    battleStrategy.resolveRound();
                });

        assertEquals(BattleStage.END, battleStrategy.getBattleStage());
        assertEquals(0, army2.getShipsLevel1().size());
        assertEquals(a2nShips, army2.getDestroyedShipsLevel1().size());
    }
}