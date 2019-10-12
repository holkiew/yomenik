package com.holkiew.yomenik.battlesim.planet.model.building;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;
import com.holkiew.yomenik.battlesim.planet.entity.CityProperties;
import com.holkiew.yomenik.battlesim.planet.entity.IronMineProperties;
import com.holkiew.yomenik.battlesim.planet.entity.Properties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumSet;
import java.util.Map;

public enum BuildingType {
    IRON_MINE("IronMine", null, IronMineProperties.class),
    CITY("City", null, CityProperties.class);

    private static final Map<String, BuildingType> ENUM_MAP;
    private String name;
    @Getter(AccessLevel.PUBLIC)
    private Properties properties;
    private Class<? extends Properties> propertiesClass;

    static {
        ENUM_MAP = EnumUtils.createEnumMap(BuildingType.class, keyMapper -> keyMapper.name.toLowerCase());
    }

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
            EnumSet.allOf(BuildingType.class)
                    .forEach(bt -> bt.properties = this.getPropertiesByClass(allProperties, bt.propertiesClass));
        }

        private Properties getPropertiesByClass(Map<String, ? extends Properties> properties, Class<? extends Properties> tClass) {
            char[] c = tClass.getSimpleName().toCharArray();
            c[0] = Character.toLowerCase(c[0]);
            var qualifier = new String(c);
            return properties.get(qualifier);
        }
    }

    @JsonCreator
    public static BuildingType get(String name) {
        return ENUM_MAP.get(name);
    }
}
