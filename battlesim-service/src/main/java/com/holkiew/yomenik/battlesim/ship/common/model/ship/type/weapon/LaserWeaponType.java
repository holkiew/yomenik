package com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;
import lombok.Getter;

import java.util.Map;

public enum LaserWeaponType implements LaserWeapon {
    LASER1("Laser1", 25),
    LASER2("Laser2", 75);

    private static final Map<String, LaserWeaponType> ENUM_MAP;

    private String name;
    @Getter
    private int baseDamagePerHit;

    LaserWeaponType(String name, int baseDamagePerHit) {
        this.baseDamagePerHit = baseDamagePerHit;
        this.name = name;
    }

    static {
        ENUM_MAP = EnumUtils.createEnumMap(LaserWeaponType.class, weaponType -> weaponType.name);
    }

    @JsonCreator
    public static LaserWeaponType get(String name) {
        return ENUM_MAP.get(name);
    }
}
