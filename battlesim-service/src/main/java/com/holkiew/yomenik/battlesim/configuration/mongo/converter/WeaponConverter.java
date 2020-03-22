package com.holkiew.yomenik.battlesim.configuration.mongo.converter;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.WeaponFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

public class WeaponConverter {

    @ReadingConverter
    public static class Read implements Converter<String, Weapon> {

        @Override
        public Weapon convert(String source) {
            return WeaponFactory.of(source)
                    .orElseThrow(() -> new RuntimeException("Cannot convert " + source + " to Weapon.class"));
        }
    }
}
