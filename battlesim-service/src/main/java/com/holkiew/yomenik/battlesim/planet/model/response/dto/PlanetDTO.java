package com.holkiew.yomenik.battlesim.planet.model.response.dto;

import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissionType;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class PlanetDTO {
    private String id;
    private String userId;
    private int galaxyId;
    private String solarSystemId;
    private Coordinates coordinates;
    private boolean isDuringBattle;
    private Resources resources;
    private Map<Integer, Building> buildings;
    private Map<String, Long> residingFleet;
    private Map<TravelMissionType, Collection<Fleet>> onRouteFleets;
}