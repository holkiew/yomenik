package com.holkiew.yomenik.battlesim.common.util;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class EnumUtils {
    public static <K extends Enum<K>, V> Collector<Map.Entry<K, V>, ?, EnumMap<K, V>> toEnumMap(Class<K> enumClass) {
        return Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (val1, val2) -> {
                    throw new RuntimeException("Not expecting duplicate keys");
                },
                () -> new EnumMap<>(enumClass));
    }

    public static <T extends Enum<T>> Map<String, T> createEnumMap(Class<T> tClass, Function<T, String> keyMapper) {
        Map<String, T> map = new ConcurrentHashMap<>();
        EnumSet<T> ts = EnumSet.allOf(tClass);
        ts.forEach(instance -> map.put(keyMapper.apply(instance), instance));
        return Collections.unmodifiableMap(map);
    }
}
