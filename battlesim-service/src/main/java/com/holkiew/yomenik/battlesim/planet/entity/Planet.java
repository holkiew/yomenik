package com.holkiew.yomenik.battlesim.planet.entity;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import com.holkiew.yomenik.battlesim.planet.model.resource.Iron;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.function.Tuple2;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Planet {
    @Id
    private String id;
    private String userId;
    private int galaxyId;
    private String solarSystemId;
    private Coordinates coordinates;
    @Builder.Default
    private boolean isDuringBattle = false;
    @Builder.Default
    private Resources resources = new Resources(new Iron(0));
    @Builder.Default
    private Map<Integer, Building> buildings = Maps.newHashMap();
    @Builder.Default
    private Map<String, Long> residingFleet = Maps.newHashMap();
    @Builder.Default
    private ListMultimap<TravelMissonType, String> onRouteFleets = MultimapBuilder.enumKeys(TravelMissonType.class).arrayListValues().build();

    public Planet(String id, String userId, int galaxyId, Tuple2<Integer, Integer> coordinates, String solarSystemId) {
        this.id = id;
        this.solarSystemId = solarSystemId;
        this.userId = userId;
        this.galaxyId = galaxyId;
        this.coordinates = new Coordinates(coordinates.getT1(), coordinates.getT2());
        this.resources = new Resources(new Iron(0));
        this.buildings = Maps.newHashMap();
        this.residingFleet = Maps.newHashMap();
        this.onRouteFleets = MultimapBuilder.enumKeys(TravelMissonType.class).arrayListValues().build();
        this.isDuringBattle = false;
    }
}
