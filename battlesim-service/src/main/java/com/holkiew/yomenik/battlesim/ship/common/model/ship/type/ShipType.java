package com.holkiew.yomenik.battlesim.ship.common.model.ship.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;

import java.util.Map;

public enum ShipType {
    SHIP_LEVEL1("shipsLevel1"),
    SHIP_LEVEL2("shipsLevel2"),
    SHIP_LEVEL3("shipsLevel3");

    private static final Map<String, ShipType> ENUM_MAP;

    private String name;

    ShipType(String name) {
        this.name = name;
    }

    static {
        ENUM_MAP = EnumUtils.createEnumMap(ShipType.class, shipType -> shipType.name);
    }

    @JsonCreator
    public static ShipType get(String name) {
        return ENUM_MAP.get(name);
    }
}
