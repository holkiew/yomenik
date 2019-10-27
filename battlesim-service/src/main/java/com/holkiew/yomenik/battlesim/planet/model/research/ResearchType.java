package com.holkiew.yomenik.battlesim.planet.model.research;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;
import com.holkiew.yomenik.battlesim.planet.model.Properties;
import com.holkiew.yomenik.battlesim.planet.model.PropertiesInjector;
import com.holkiew.yomenik.battlesim.planet.model.research.properites.LaserProperties;
import com.holkiew.yomenik.battlesim.planet.model.research.properites.ResearchProperties;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;


public enum ResearchType {
    LASER("Laser", null, LaserProperties.class);

    private static final Map<String, ResearchType> ENUM_MAP;
    private String name;
    @Getter(AccessLevel.PUBLIC)
    private ResearchProperties properties;
    private Class<? extends Properties> propertiesClass;

    static {
        ENUM_MAP = EnumUtils.createEnumMap(ResearchType.class, keyMapper -> keyMapper.name);
    }

    ResearchType(String name, ResearchProperties properties, Class<? extends Properties> propertiesClass) {
        this.name = name;
        this.properties = properties;
        this.propertiesClass = propertiesClass;
    }

    @JsonCreator
    public static ResearchType get(String name) {
        return ENUM_MAP.get(name);
    }

    @Component
    public static class ResearchTypePropertiesInjector extends PropertiesInjector {
        public ResearchTypePropertiesInjector(Map<String, ? extends Properties> allProperties) {
            super(allProperties);
        }

        public void postConstruct() {
            EnumSet.allOf(ResearchType.class)
                    .forEach(rt -> rt.properties = (ResearchProperties) this.getPropertiesByClass(allProperties, rt.propertiesClass));
        }
    }
}
