package com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon;

public interface KineticWeapon extends Weapon {
    default WeaponType getWeaponType() {
        return WeaponType.KINETIC;
    }
}
