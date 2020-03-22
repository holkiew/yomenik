package com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeaponFactory {

    private static Map<String, Weapon> WEAPON_ENUM_MAPS;

    static {
        WEAPON_ENUM_MAPS = Stream.of(
                EnumSet.allOf(KineticWeaponType.class),
                EnumSet.allOf(LaserWeaponType.class))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Enum::name, Function.identity()));
    }

    public static Optional<Weapon> of(String weaponName) {
        return Optional.ofNullable(WEAPON_ENUM_MAPS.get(weaponName));
    }

}
