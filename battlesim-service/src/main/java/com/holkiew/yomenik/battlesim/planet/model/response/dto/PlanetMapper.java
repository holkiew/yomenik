package com.holkiew.yomenik.battlesim.planet.model.response.dto;

import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.battlesim.common.util.MultimapCollector;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissionType;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PlanetMapper {

    @Mapping(source = "onRouteFleets", target = "onRouteFleets", qualifiedByName = "onRouteFleets")
    @Mapping(source = "availableBuildings", target = "availableBuildings", qualifiedByName = "availableBuildings")
    @Mapping(source = "buildings", target = "buildings", qualifiedByName = "buildings")
    PlanetDTO mapToDTO(Planet planet, @Context Set<Fleet> fleet);

    List<PlanetDTO> mapToDTOList(Collection<Planet> planets, @Context Set<Fleet> fleet);

    @Named("onRouteFleets")
    default Map<TravelMissionType, Collection<Fleet>> mapOnRouteFleets(ListMultimap<TravelMissionType, String> multimap, @Context Set<Fleet> fleet) {
        Map<String, Fleet> collect = fleet.stream().map(f -> Map.entry(f.getId(), f)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return multimap.entries().stream().map(entry -> Map.entry(entry.getKey(), collect.get(entry.getValue())))
                .collect(MultimapCollector.toMultimap(Map.Entry::getKey, Map.Entry::getValue)).asMap();
    }

    @Named("availableBuildings")
    default Set<AvailableBuildingTypeDTO> mapAvailableBuildings(Set<BuildingType> availableBuildings) {
        return availableBuildings.stream().map(AvailableBuildingTypeDTO::new).collect(Collectors.toSet());
    }

    @Named("buildings")
    default Map<Integer, BuildingDTO> mapBuildings(Map<Integer, Building> buildings) {
        return buildings.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new BuildingDTO(entry.getValue())));
    }
}