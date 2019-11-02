package com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;
import lombok.Getter;

import java.util.Map;

public enum KineticWeaponType implements KineticWeapon {
    KINETIC1("Kinetic1", 25);

    private static final Map<String, KineticWeaponType> ENUM_MAP;

    private String name;
    @Getter
    private int baseDamagePerHit;

    KineticWeaponType(String name, int baseDamagePerHit) {
        this.baseDamagePerHit = baseDamagePerHit;
        this.name = name;
    }

    static {
        ENUM_MAP = EnumUtils.createEnumMap(KineticWeaponType.class, weaponType -> weaponType.name);
    }

    @JsonCreator
    public static KineticWeaponType get(String name) {
        return ENUM_MAP.get(name);
    }
}
