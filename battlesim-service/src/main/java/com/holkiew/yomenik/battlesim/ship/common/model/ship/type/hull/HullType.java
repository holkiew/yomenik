package com.holkiew.yomenik.battlesim.ship.common.model.ship.type.hull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;
import lombok.Getter;

import java.util.Map;

public enum HullType {
    SHIP_LEVEL1("ShipLevel1", 100, 0, 0),
    SHIP_LEVEL2("ShipLevel2", 250, 25, 100),
    SHIP_LEVEL3("ShipLevel3", 1000, 250, 250);

    private static final Map<String, HullType> ENUM_MAP;
    private String name;
    @Getter
    private int baseHealthPoints;
    @Getter
    private int baseArmorPoints;
    @Getter
    private int baseShieldPoints;

    HullType(String name, int baseHealthPoints, int baseArmorPoints, int baseShieldPoints) {
        this.name = name;
        this.baseHealthPoints = baseHealthPoints;
        this.baseArmorPoints = baseArmorPoints;
        this.baseShieldPoints = baseShieldPoints;
    }


    static {
        ENUM_MAP = EnumUtils.createEnumMap(HullType.class, weaponType -> weaponType.name);
    }

    @JsonCreator
    public static HullType get(String name) {
        return ENUM_MAP.get(name);
    }
}





