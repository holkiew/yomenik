package com.holkiew.yomenik.battlesim.planet.model.response.dto;

import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissionType;
import lombok.Data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Data
public class PlanetDTO {
    String id;
    String userId;
    int galaxyId;
    String solarSystemId;
    Coordinates coordinates;
    boolean isDuringBattle;
    Resources resources;
    Map<Integer, Building> buildings;
    Set<AvailableBuildingType> availableBuildings;
    Map<String, Long> residingFleet;
    Map<TravelMissionType, Collection<Fleet>> onRouteFleets;
}