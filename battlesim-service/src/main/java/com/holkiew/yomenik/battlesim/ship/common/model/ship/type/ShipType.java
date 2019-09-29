package com.holkiew.yomenik.battlesim.ship.common.model.ship.type;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        Map<String, ShipType> map = new ConcurrentHashMap<>();
        for (ShipType instance : ShipType.values()) {
            map.put(instance.name, instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    @JsonCreator
    public static ShipType get(String name) {
        return ENUM_MAP.get(name);
    }
}
