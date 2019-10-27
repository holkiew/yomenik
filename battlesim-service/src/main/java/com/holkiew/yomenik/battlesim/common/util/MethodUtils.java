package com.holkiew.yomenik.battlesim.common.util;

import java.util.function.Consumer;

public class MethodUtils {
    public static <T> void updateValue(Consumer<T> setterMethod, T value) {
        if (value != null) {
            setterMethod.accept(value);
        }
    }
}
