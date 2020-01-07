package com.holkiew.yomenik.battlesim.planet.model.response.dto;

import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Data
public class PlanetDTO {
    private String id;
    private String userId;
    private int galaxyId;
    private String solarSystemId;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private int buildingSlots;
    private boolean isDuringBattle;
    private Resources resources;
    private Map<Integer, BuildingDTO> buildings;
    private Set<AvailableBuildingTypeDTO> availableBuildings;
    private Map<String, Long> residingFleet;
    private Map<TravelMissionType, Collection<Fleet>> onRouteFleets;
}