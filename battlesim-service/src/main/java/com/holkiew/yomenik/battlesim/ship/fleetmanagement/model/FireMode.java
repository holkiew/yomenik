package com.holkiew.yomenik.battlesim.ship.fleetmanagement.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.WeaponType;

import java.util.Map;

public enum FireMode {
    SCATTER, FIXED;

    private static final Map<String, WeaponType> ENUM_MAP;

    static {
        ENUM_MAP = EnumUtils.createEnumMap(WeaponType.class, Enum::toString);
    }

    @JsonCreator
    public static WeaponType get(String name) {
        return ENUM_MAP.get(name);
    }
}
