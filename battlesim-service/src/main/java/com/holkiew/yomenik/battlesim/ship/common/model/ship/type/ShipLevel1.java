package com.holkiew.yomenik.battlesim.ship.common.model.ship.type;

import com.holkiew.yomenik.battlesim.common.model.ShipClassType;

public class ShipLevel1 extends Ship {

    public ShipLevel1(Integer healthPoints, Integer shieldsPoints, Integer armorPoints) {
        super(healthPoints, shieldsPoints, armorPoints, ShipClassType.SHIP_LEVEL1);
    }
}
