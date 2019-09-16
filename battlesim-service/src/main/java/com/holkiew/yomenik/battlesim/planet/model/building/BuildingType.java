package com.holkiew.yomenik.battlesim.planet.model.building;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum BuildingType {
    MINE("mine"),
    CITY("city");

    private static final Map<String, BuildingType> ENUM_MAP;

    private String name;

    BuildingType(String name) {
        this.name = name;
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
