package com.holkiew.yomenik.battlesim.configuration.jackson.serializer.generic;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.WeaponFactory;

import java.io.IOException;

public class WeaponDeserializer extends StdDeserializer<Weapon> {

    public WeaponDeserializer() {
        super(Weapon.class);
    }

    @Override
    public Weapon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        var valueAsString = p.getValueAsString();
        return WeaponFactory.of(valueAsString)
                .orElseThrow(() -> new JsonParseException(p, "Cannot convert " + valueAsString + " to Weapon.class"));
    }
}