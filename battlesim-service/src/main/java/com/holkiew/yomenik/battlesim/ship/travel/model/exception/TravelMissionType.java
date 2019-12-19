package com.holkiew.yomenik.battlesim.ship.travel.model.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;

import java.util.Map;

public enum TravelMissionType {
    ATTACK, ATTACK_BATTLE, TRANSFER, TRANSFER_BACK, MOVE;

    private static final Map<String, TravelMissionType> ENUM_MAP;

    static {
        ENUM_MAP = EnumUtils.createEnumMap(TravelMissionType.class, Enum::name);
    }

    @JsonCreator
    public static TravelMissionType get(String name) {
        return ENUM_MAP.get(name);
    }
}
