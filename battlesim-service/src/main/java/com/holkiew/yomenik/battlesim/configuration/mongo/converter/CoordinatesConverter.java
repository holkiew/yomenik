package com.holkiew.yomenik.battlesim.configuration.mongo.converter;

import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import javax.swing.text.Document;

public class CoordinatesConverter {

    @WritingConverter
    public static class Write implements Converter<Coordinates, String> {
        @Override
        public String convert(Coordinates source) {
            return source.x + ", " + source.y;
        }
    }

    @ReadingConverter
    public static class Read implements Converter<Document, Coordinates> {
        @Override
        public Coordinates convert(Document source) {
            return new Coordinates(999, 999);
        }
    }
}
