package com.holkiew.yomenik.battlesim.planet.model.building;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum BuildingType {
    MINE("ironMine", IronMine.class),
    CITY("city", City.class);

    private static final Map<String, BuildingType> ENUM_MAP;

    private String name;
    @Getter
    private Class<? extends Building> tClass;

    BuildingType(String name, Class<? extends Building> tClass) {
        this.name = name;
        this.tClass = tClass;
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
