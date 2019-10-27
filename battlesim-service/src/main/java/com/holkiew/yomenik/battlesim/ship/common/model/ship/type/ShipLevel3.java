package com.holkiew.yomenik.battlesim.ship.common.model.ship.type;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;

import java.util.List;

public class ShipLevel3 extends Ship {

    public ShipLevel3(Integer healthPoints, Integer shieldsPoints, Integer armorPoints, List<Weapon> weaponTypes) {
        super(healthPoints, shieldsPoints, armorPoints, weaponTypes);
    }
}
