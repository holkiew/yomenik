package com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;

import java.util.Map;

public enum WeaponType {
    LASER("Laser"),
    KINETIC("Kinetic");

    private static final Map<String, WeaponType> ENUM_MAP;
    private String name;

    WeaponType(String name) {
        this.name = name;
    }

    static {
        ENUM_MAP = EnumUtils.createEnumMap(WeaponType.class, weaponType -> weaponType.name);
    }

    @JsonCreator
    public static WeaponType get(String name) {
        return ENUM_MAP.get(name);
    }
}





