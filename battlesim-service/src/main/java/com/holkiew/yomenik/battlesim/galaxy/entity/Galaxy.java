package com.holkiew.yomenik.battlesim.galaxy.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.function.Tuple2;

import java.util.Map;

@Document
@Data
@NoArgsConstructor
public class Galaxy {

    @Id
    private int id;
    // no complex objects as keys
    // String as x, y, String ->  planetId

    private Map<String, String> planetsCoordinates;

    public static String toPlanetCoordinatesString(Tuple2<Integer, Integer> coordinates) {
        return toPlanetCoordinatesString(coordinates.getT1(), coordinates.getT2());
    }

    public static String toPlanetCoordinatesString(int x, int y) {
        return x + ", " + y;
    }
}
