package com.holkiew.yomenik.battlesim.configuration.mongo.insert;

import com.google.common.collect.Maps;
import com.holkiew.yomenik.battlesim.common.MongoInsertsLoader;
import com.holkiew.yomenik.battlesim.galaxy.entity.SolarSystem;
import com.holkiew.yomenik.battlesim.galaxy.port.SolarSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SolarSystemInserts extends MongoInsertsLoader<SolarSystem, SolarSystemRepository> {

    public SolarSystemInserts(@Autowired SolarSystemRepository repository) {
        super(repository);
        this.checkIfObjectAlreadyExistsFun = solarSystem -> repository.existsById(solarSystem.getId());
        setData();
    }

    private void setData() {
        var solarSystem1 = SolarSystem.builder().id("1").galaxyId(1).nextSolarSystemId("2").build();
        var solarSystem2 = SolarSystem.builder().id("2").galaxyId(1).nextSolarSystemId("3").prevSolarSystemId("1").build();
        var solarSystem3 = SolarSystem.builder().id("3").galaxyId(1).prevSolarSystemId("2").build();
        this.setData(solarSystem1, solarSystem2, solarSystem3);
        Arrays.stream(this.data).forEach(ss -> ss.setPlanetsCoordinatesIds(Maps.newHashMap()));
    }
}