package com.holkiew.yomenik.battlesim.configuration.mongo.insert;

import com.holkiew.yomenik.battlesim.common.MongoInsertsLoader;
import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.port.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuples;

import java.util.HashMap;

@Component
public class PlanetInserts extends MongoInsertsLoader<Planet, PlanetRepository> {

    public PlanetInserts(@Autowired PlanetRepository repository) {
        super(repository);
        this.checkIfObjectAlreadyExistsFun = planet -> repository.existsById(planet.getId());
        setData();
    }

    private void setData() {
        var planet1ResidingFleet = new HashMap<String, Long>();
        planet1ResidingFleet.put("SHIP_LEVEL1_template", 100L);
        planet1ResidingFleet.put("SHIP_LEVEL3_template", 10L);
        Planet planet1 = Planet.builder()
                .id("1").userId("1").galaxyId(1).coordinates(new Coordinates(1, 1))
                .solarSystemId("1")
                .residingFleet(planet1ResidingFleet)
                .build();
        Planet planet2 = new Planet("2", "1", 1, Tuples.of(1, 2), "1");
        setData(planet1, planet2);
    }
}
