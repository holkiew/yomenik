package com.holkiew.yomenik.battlesim.planet.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.holkiew.yomenik.battlesim.configuration.jackson.serializer.OnRouteFleetsSerializer;
import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import com.holkiew.yomenik.battlesim.planet.model.resource.Iron;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private Map<Integer, Building> buildings = new HashMap<>();
    @Builder.Default
    private Set<BuildingType> availableBuildings = new HashSet<>();
    @Builder.Default
    private Map<String, Long> residingFleet = new HashMap<>();
    @Builder.Default
    @JsonSerialize(using = OnRouteFleetsSerializer.class)
    private ListMultimap<TravelMissionType, String> onRouteFleets = MultimapBuilder.enumKeys(TravelMissionType.class).arrayListValues().build();
}
