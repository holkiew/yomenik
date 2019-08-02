package com.holkiew.yomenik.simulator;

import org.junit.Test;

import java.util.HashMap;

import static com.holkiew.yomenik.simulator.ships.ShipName.*;
import static org.junit.Assert.assertEquals;

public class ArmyTest {

    @Test
    public void testOf() {
        long shipl1 = 3, shipl3 = 15;
        Army army = Army.of(new HashMap<>() {{
            put(SHIP_LEVEL1, shipl1);
            put(SHIP_LEVEL3, shipl3);
        }});

        assertEquals(0, army.getDestroyedShips().values().size());
        assertEquals(shipl1, army.getShips().get(SHIP_LEVEL1).size());
        assertEquals(shipl3, army.getShips().get(SHIP_LEVEL3).size());
        assertEquals(0, army.getShips().get(SHIP_LEVEL2).size());
        assertEquals(shipl1 + shipl3, army.getShips().values().size());
    }
}
