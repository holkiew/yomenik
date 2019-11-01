package com.holkiew.yomenik.battlesim.configuration.mongo.converter;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissonType;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class TravelMissionTypeMultimapConverter implements Converter<Document, ListMultimap<TravelMissonType, String>> {

    @Override
    public ListMultimap<TravelMissonType, String> convert(Document source) {
        var map = MultimapBuilder.enumKeys(TravelMissonType.class).arrayListValues().<TravelMissonType, String>build();
        source.forEach((name, id) -> {
            TravelMissonType key = TravelMissonType.get(name);
            if (id instanceof Iterable) {
                map.putAll(key, (Iterable) id);
            } else {
                map.put(key, (String) id);
            }
        });
        return map;
    }

}
