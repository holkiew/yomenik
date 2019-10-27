package com.holkiew.yomenik.battlesim.planet.model;

import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.Map;

@RequiredArgsConstructor
public abstract class PropertiesInjector {
    protected final Map<String, ? extends Properties> allProperties;

    @PostConstruct
    public abstract void postConstruct();

    protected Properties getPropertiesByClass(Map<String, ? extends Properties> properties, Class<? extends Properties> tClass) {
        char[] c = tClass.getSimpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        var qualifier = new String(c);
        return properties.get(qualifier);
    }
}
