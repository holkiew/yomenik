package com.holkiew.yomenik.battlesim.ship.travel.model.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;

import java.util.Map;

public enum TravelMissonType {
    ATTACK, TRANSFER, TRANSFER_BACK, MOVE;

    private static final Map<String, TravelMissonType> ENUM_MAP;

    static {
        ENUM_MAP = EnumUtils.createEnumMap(TravelMissonType.class, keyMapper -> keyMapper.name().toLowerCase());
    }

    @JsonCreator
    public static TravelMissonType get(String name) {
        return ENUM_MAP.get(name);
    }
}
