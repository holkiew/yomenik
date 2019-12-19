package com.holkiew.yomenik.battlesim.configuration.mongo.converter;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WritingConverter
public class MultimapEnumKeyConverter implements Converter<ListMultimap<? extends Enum, ?>, Map<String, List<?>>> {
    @Override
    public Map<String, List<?>> convert(ListMultimap<? extends Enum, ?> source) {
        Map<String, List<?>> classicMap = new HashMap<>();
        Multimaps.asMap(source).forEach((key, value) -> classicMap.put(key.toString(), value));
        return classicMap;
    }
}
