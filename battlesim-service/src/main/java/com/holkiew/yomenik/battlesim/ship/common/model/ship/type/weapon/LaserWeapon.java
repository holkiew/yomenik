package com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon;

public interface LaserWeapon extends Weapon {

    default WeaponType getWeaponType() {
        return WeaponType.LASER;
    }
}
