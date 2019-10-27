package com.holkiew.yomenik.battlesim.ship.common.model.ship.type;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;

import java.util.List;

public class ShipLevel1 extends Ship {

    public ShipLevel1(Integer healthPoints, Integer shieldsPoints, Integer armorPoints, List<Weapon> weaponTypes) {
        super(healthPoints, shieldsPoints, armorPoints, weaponTypes);
    }
}
