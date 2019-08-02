package com.holkiew.yomenik.simulator.ships;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ShipName {
    SHIP_LEVEL1("shipsLevel1"),
    SHIP_LEVEL2("shipsLevel2"),
    SHIP_LEVEL3("shipsLevel3");

    private static final Map<String, ShipName> ENUM_MAP;

    private String name;

    ShipName(String name) {
        this.name = name;
    }

    static {
        Map<String, ShipName> map = new ConcurrentHashMap<>();
        for (ShipName instance : ShipName.values()) {
            map.put(instance.name, instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    @JsonCreator
    public static ShipName get(String name) {
        return ENUM_MAP.get(name);
    }
}
