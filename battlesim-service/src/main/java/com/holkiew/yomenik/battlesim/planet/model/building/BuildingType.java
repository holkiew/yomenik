package com.holkiew.yomenik.battlesim.planet.model.building;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;
import com.holkiew.yomenik.battlesim.planet.model.Properties;
import com.holkiew.yomenik.battlesim.planet.model.PropertiesInjector;
import com.holkiew.yomenik.battlesim.planet.model.building.properties.BuildingProperties;
import com.holkiew.yomenik.battlesim.planet.model.building.properties.CityProperties;
import com.holkiew.yomenik.battlesim.planet.model.building.properties.IronMineProperties;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;

public enum BuildingType {
    IRON_MINE("IronMine", null, IronMineProperties.class),
    CITY("City", null, CityProperties.class);

    private static final Map<String, BuildingType> ENUM_MAP;
    private String name;
    @Getter(AccessLevel.PUBLIC)
    private BuildingProperties properties;
    private Class<? extends Properties> propertiesClass;

    static {
        ENUM_MAP = EnumUtils.createEnumMap(BuildingType.class, keyMapper -> keyMapper.name);
    }

    BuildingType(String name, BuildingProperties properties, Class<? extends Properties> propertiesClass) {
        this.name = name;
        this.properties = properties;
        this.propertiesClass = propertiesClass;
    }

    @JsonCreator
    public static BuildingType get(String name) {
        return ENUM_MAP.get(name);
    }

    @Component
    public static class BuildingTypePropertiesInjector extends PropertiesInjector {
        public BuildingTypePropertiesInjector(Map<String, ? extends Properties> allProperties) {
            super(allProperties);
        }

        public void postConstruct() {
            EnumSet.allOf(BuildingType.class)
                    .forEach(bt -> bt.properties = (BuildingProperties) this.getPropertiesByClass(allProperties, bt.propertiesClass));
        }
    }
}
