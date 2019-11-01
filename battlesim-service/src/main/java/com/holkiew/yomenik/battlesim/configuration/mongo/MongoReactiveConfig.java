package com.holkiew.yomenik.battlesim.configuration.mongo;

import com.holkiew.yomenik.battlesim.configuration.mongo.converter.CoordinatesConverter;
import com.holkiew.yomenik.battlesim.configuration.mongo.converter.MultimapEnumKeyConverter;
import com.holkiew.yomenik.battlesim.configuration.mongo.converter.TravelMissionTypeMultimapConverter;
import com.holkiew.yomenik.battlesim.configuration.mongo.converter.WeaponConverter;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@EnableReactiveMongoRepositories
public class MongoReactiveConfig extends AbstractReactiveMongoConfiguration {

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Configuration
    public static class MongoConfig {

        @Bean
        public MongoCustomConversions customConversions() {
            List<Converter<?, ?>> converters = new ArrayList<>();
            converters.add(new MultimapEnumKeyConverter());
            converters.add(new TravelMissionTypeMultimapConverter());
            converters.add(new CoordinatesConverter.Read());
            converters.add(new CoordinatesConverter.Write());
            converters.add(new WeaponConverter.Read());
            return new MongoCustomConversions(converters);
        }
    }
}
