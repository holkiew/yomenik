package com.holkiew.yomenik.battlesim.ship.common.model.ship.type.hull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.holkiew.yomenik.battlesim.common.model.ShipClassType;
import com.holkiew.yomenik.battlesim.common.util.EnumUtils;
import lombok.Getter;

import java.util.Map;

public enum HullType {
    SHIP_LEVEL1(ShipClassType.SHIP_LEVEL1, 100, 0, 0),
    SHIP_LEVEL2(ShipClassType.SHIP_LEVEL2, 250, 25, 100),
    SHIP_LEVEL3(ShipClassType.SHIP_LEVEL3, 1000, 250, 250);

    private static final Map<String, HullType> ENUM_MAP;
    @Getter
    private int baseHealthPoints;
    @Getter
    private ShipClassType shipClassType;
    @Getter
    private int baseArmorPoints;
    @Getter
    private int baseShieldPoints;

    HullType(ShipClassType shipClassType, int baseHealthPoints, int baseArmorPoints, int baseShieldPoints) {
        this.shipClassType = shipClassType;
        this.baseHealthPoints = baseHealthPoints;
        this.baseArmorPoints = baseArmorPoints;
        this.baseShieldPoints = baseShieldPoints;
    }

    static {
        ENUM_MAP = EnumUtils.createEnumMap(HullType.class, Enum::toString);
    }

    @JsonCreator
    public static HullType get(String name) {
        return ENUM_MAP.get(name);
    }
}





