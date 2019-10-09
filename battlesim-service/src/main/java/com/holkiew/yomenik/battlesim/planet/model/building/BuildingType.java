package com.holkiew.yomenik.battlesim.planet.model.building;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import com.holkiew.yomenik.battlesim.planet.entity.CityProperties;
import com.holkiew.yomenik.battlesim.planet.entity.IronMineProperties;
import com.holkiew.yomenik.battlesim.planet.entity.Properties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum BuildingType {
    IRON_MINE("IronMine", null, IronMineProperties.class),
    CITY("City", null, CityProperties.class);

    private static final Map<String, BuildingType> ENUM_MAP;
    private String name;
    @Getter(AccessLevel.PUBLIC)
    private Properties properties;
    private Class<? extends Properties> propertiesClass;

    BuildingType(String name, Properties properties, Class<? extends Properties> propertiesClass) {
        this.name = name;
        this.properties = properties;
        this.propertiesClass = propertiesClass;
    }

    @Component
    @RequiredArgsConstructor
    public static class PropertiesInjector {
        private final Map<String, ? extends Properties> allProperties;

        @PostConstruct
        public void postConstruct() {
            for (BuildingType bt : EnumSet.allOf(BuildingType.class)) {
                bt.properties = this.getPropertiesByClass(allProperties, bt.propertiesClass);
            }
        }

        private <T> T getPropertiesByClass(Map<String, ? extends T> properties, Class tClass){
            char[] c = tClass.getSimpleName().toCharArray();
            c[0] = Character.toLowerCase(c[0]);
            var qualifier = new String(c);
            return properties.get(qualifier);
        }
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
