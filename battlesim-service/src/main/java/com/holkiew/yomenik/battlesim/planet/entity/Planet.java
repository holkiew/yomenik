package com.holkiew.yomenik.battlesim.planet.entity;

import com.google.common.collect.Maps;
import com.holkiew.yomenik.battlesim.planet.model.resource.Iron;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.UUID;

@Data
@Document
@NoArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "galaxy_coordinates", unique = true, def = "{'galaxyId' : 1, 'coordinateX': 1, 'coordinateY': 1}")
})
public class Planet {

    @Id
    private String id;
    private String userId;
    private int galaxyId;
    private int coordinateX;
    private int coordinateY;
    private Resources resources;
    private Map<Integer, Building> buildings;

    public Planet(String userId, int galaxyId, Tuple2<Integer, Integer> coordinates) {
        id = UUID.randomUUID().toString();
        this.userId = userId;
        this.galaxyId = galaxyId;
        this.coordinateX = coordinates.getT1();
        this.coordinateY = coordinates.getT2();
        this.buildings = Maps.newHashMap();
        this.resources = new Resources(new Iron(0));
    }
}
