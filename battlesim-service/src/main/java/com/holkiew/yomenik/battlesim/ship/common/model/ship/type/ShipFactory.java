package com.holkiew.yomenik.battlesim.ship.common.model.ship.type;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.LaserWeaponType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShipFactory {

    public static Optional<Ship> getShip(ShipClassType shipClassType) {
        switch (shipClassType) {
            case SHIP_LEVEL1:
                return Optional.of(getShipLevel1());
            case SHIP_LEVEL2:
                return Optional.of(getShipLevel2());
            case SHIP_LEVEL3:
                return Optional.of(getShipLevel3());
            default:
                return Optional.empty();
        }
    }

    public static ShipLevel1 getShipLevel1() {
        return new ShipLevel1(100, 0, 0, Collections.singletonList(LaserWeaponType.LASER1));
    }

    public static ShipLevel2 getShipLevel2() {
        return new ShipLevel2(250, 25, 100, Arrays.asList(LaserWeaponType.LASER1, LaserWeaponType.LASER1));
    }

    public static ShipLevel3 getShipLevel3() {
        return new ShipLevel3(1000, 250, 1000, Stream.generate(() -> LaserWeaponType.LASER1).limit(5).collect(Collectors.toList()));
    }
}
