package com.holkiew.yomenik.battlesim.util;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UtilMethods {
    public static <K extends Enum<K>, V> Collector<Map.Entry<K, V>, ?, EnumMap<K, V>> toEnumMap(Class<K> enumClass) {
        return Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (val1, val2) -> {
                    throw new RuntimeException("Not expecting duplicate keys");
                },
                () -> new EnumMap<>(enumClass));
    }
}
