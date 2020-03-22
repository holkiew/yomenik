package com.holkiew.yomenik.battlesim.configuration.jackson.serializer.distinct;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissionType;

import java.io.IOException;

public class OnRouteFleetsSerializer extends StdSerializer<ListMultimap> {

    public OnRouteFleetsSerializer() {
        super(ListMultimap.class);
    }

    @Override
    public void serialize(ListMultimap multimap, JsonGenerator gen, SerializerProvider provider) throws IOException {
        var typedMultimap = ((ListMultimap<TravelMissionType, String>) multimap);
        gen.writeStartObject();
        typedMultimap.asMap().forEach((travelMissonType, ships) -> {
            try {
                gen.writeStringField(travelMissonType.name(), ships.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        gen.writeEndObject();
    }

}