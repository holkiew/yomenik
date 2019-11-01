package com.holkiew.yomenik.battlesim.configuration.mongo.converter;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.KineticWeaponType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.LaserWeaponType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeaponConverter {

    @ReadingConverter
    public static class Read implements Converter<String, Weapon> {
        private Map<String, Weapon> WEAPON_ENUM_MAPS;

        public Read() {
            WEAPON_ENUM_MAPS = Stream.of(
                    EnumSet.allOf(KineticWeaponType.class),
                    EnumSet.allOf(LaserWeaponType.class))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toMap(Enum::name, Function.identity()));
        }

        @Override
        public Weapon convert(String source) {
            return WEAPON_ENUM_MAPS.get(source);
        }
    }
}
