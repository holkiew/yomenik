package com.holkiew.yomenik.battlesim.planet.entity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissonType;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WritingConverter
@Configuration
public class PlanetConverter implements Converter<ListMultimap<? extends Enum, String>, Map<String, List<String>>> {

    @Override
    public Map<String, List<String>> convert(ListMultimap<? extends Enum, String> source) {
        Map<String, List<String>> classicMap = new HashMap<>();
        Multimaps.asMap(source).forEach((key, value) -> classicMap.put(key.toString().toLowerCase(), value));
        return classicMap;
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new PlanetConverter());
        converters.add(new MapConverter());
        return new MongoCustomConversions(converters);
    }

    @ReadingConverter
    public class MapConverter implements Converter<Document, ListMultimap<TravelMissonType, String>> {

        @Override
        public ListMultimap<TravelMissonType, String> convert(Document source) {
            ArrayListMultimap<TravelMissonType, String> map = ArrayListMultimap.create();
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

}
