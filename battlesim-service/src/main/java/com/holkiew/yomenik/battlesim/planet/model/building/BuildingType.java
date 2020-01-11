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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;

public enum BuildingType {
    IRON_MINE("iron_mine", "Iron mine", null, IronMineProperties.class),
    CRYSTAL_MINE("crystal_mine", "Crystal mine", null, IronMineProperties.class),
    CONCRETE_FACTORY("concrete_factory", "Concrete factory", null, IronMineProperties.class),
    CITY("city", "Iron mine", null, CityProperties.class);

    private static final Map<String, BuildingType> ENUM_MAP;
    @Getter(AccessLevel.PUBLIC)
    private String name;
    @Getter(AccessLevel.PUBLIC)
    private String label;
    @Getter(AccessLevel.PUBLIC)
    private BuildingProperties properties;
    private Class<? extends Properties> propertiesClass;

    static {
        ENUM_MAP = EnumUtils.createEnumMap(BuildingType.class, keyMapper -> keyMapper.name);
    }

    BuildingType(String name, String label, BuildingProperties properties, Class<? extends Properties> propertiesClass) {
        this.name = name;
        this.label = label;
        this.properties = properties;
        this.propertiesClass = propertiesClass;
    }

    @JsonCreator
    public static BuildingType get(String name) {
        return ENUM_MAP.get(name);
    }

    @Component
    public static class BuildingTypePropertiesInjector extends PropertiesInjector {
        public BuildingTypePropertiesInjector(Map<String, ? extends Properties> allProperties, ApplicationContext appCtx) {
            super(allProperties);
        }

        public void postConstruct() {
            EnumSet.allOf(BuildingType.class)
                    .forEach(bt -> bt.properties = (BuildingProperties) this.getPropertiesByClass(allProperties, bt.propertiesClass));
        }
    }
}
