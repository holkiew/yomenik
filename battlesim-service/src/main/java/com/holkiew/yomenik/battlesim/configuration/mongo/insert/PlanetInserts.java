package com.holkiew.yomenik.battlesim.configuration.mongo.insert;

import com.holkiew.yomenik.battlesim.common.MongoInsertsLoader;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.port.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanetInserts extends MongoInsertsLoader<Planet, PlanetRepository> {

    public PlanetInserts(@Autowired PlanetRepository repository) {
        super(repository);
        this.checkIfObjectAlreadyExistsFun = planet -> repository.existsById(planet.getId());
    }
}
