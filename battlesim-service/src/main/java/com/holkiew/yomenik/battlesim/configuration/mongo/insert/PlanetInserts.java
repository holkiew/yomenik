package com.holkiew.yomenik.battlesim.configuration.mongo.insert;

import com.holkiew.yomenik.battlesim.common.MongoInsertsLoader;
import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.port.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.holkiew.yomenik.battlesim.planet.model.building.BuildingType.*;

@Component
public class PlanetInserts extends MongoInsertsLoader<Planet, PlanetRepository> {

    public PlanetInserts(@Autowired PlanetRepository repository) {
        super(repository);
        this.checkIfObjectAlreadyExistsFun = planet -> repository.existsById(planet.getId());
        setData();
    }

    private void setData() {
        var planet1 = Planet.builder()
                .id("1").userId("1").galaxyId(1).coordinates(new Coordinates(1, 1)).solarSystemId("1")
                .residingFleet(Map.of(
                        "SHIP_LEVEL1_template", 100L,
                        "SHIP_LEVEL3_template", 10L))
                .availableBuildings(Set.of(IRON_MINE, CONCRETE_FACTORY, CRYSTAL_MINE))
                .buildings(Map.of(1, Building.builder().id(1).level(1).slot(1).buildingType(IRON_MINE).build()))
                .buildingSlots(3)
                .build();
        var planet2 = Planet.builder()
                .id("2").userId("1").galaxyId(1).coordinates(new Coordinates(1, 2)).solarSystemId("1")
                .availableBuildings(Set.of(IRON_MINE))
                .buildingSlots(6)
                .build();
        setData(planet1, planet2);
    }
}
