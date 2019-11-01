package com.holkiew.yomenik.battlesim.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;

import java.util.Map;

public enum ShipClassType {
    SHIP_LEVEL1("shipsLevel1"),
    SHIP_LEVEL2("shipsLevel2"),
    SHIP_LEVEL3("shipsLevel3");

    private static final Map<String, ShipClassType> ENUM_MAP;

    private String name;

    ShipClassType(String name) {
        this.name = name;
    }

    static {
        ENUM_MAP = EnumUtils.createEnumMap(ShipClassType.class, shipType -> shipType.name);
    }

    @JsonCreator
    public static ShipClassType get(String name) {
        return ENUM_MAP.get(name);
    }
}
