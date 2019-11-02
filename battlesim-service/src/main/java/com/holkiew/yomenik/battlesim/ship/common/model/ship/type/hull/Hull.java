package com.holkiew.yomenik.battlesim.ship.common.model.ship.type.hull;


import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Hull {
    private Map<Integer, Weapon> weaponSlots;
    private Integer healthPoints;
    private Integer shieldsPoints;
    private Integer armorPoints;

    public Hull(HullType hullType, Map<Integer, Weapon> weaponSlots) {
        this.weaponSlots = weaponSlots;
        this.healthPoints = hullType.getBaseHealthPoints();
        this.shieldsPoints = hullType.getBaseShieldPoints();
        this.armorPoints = hullType.getBaseArmorPoints();
    }
}
