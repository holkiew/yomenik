package com.holkiew.yomenik.battlesim.planet.model.building;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum BuildingType {
    MINE("ironMine", IronMine.class),
    CITY("city", City.class);

    private static final Map<String, BuildingType> ENUM_MAP;

    private String name;
    private Class klass;

    BuildingType(String name, Class klass) {
        this.name = name;
        this.klass = klass;
    }

    static {
        Map<String, BuildingType> map = new ConcurrentHashMap<>();
        Arrays.stream(BuildingType.values()).forEach(instance -> map.put(instance.name, instance));
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    @JsonCreator
    public static BuildingType get(String name) {
        return ENUM_MAP.get(name);
    }
}
