package com.holkiew.yomenik.battlesim.configuration.mongo;

import com.holkiew.yomenik.battlesim.configuration.mongo.insert.GalaxyInserts;
import com.holkiew.yomenik.battlesim.configuration.mongo.insert.PlanetInserts;
import com.holkiew.yomenik.battlesim.configuration.mongo.insert.ResearchInserts;
import com.holkiew.yomenik.battlesim.configuration.mongo.insert.SolarSystemInserts;
import com.holkiew.yomenik.battlesim.galaxy.entity.Galaxy;
import com.holkiew.yomenik.battlesim.galaxy.entity.SolarSystem;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class InitialInserts {

    private final PlanetInserts planetInserts;
    private final GalaxyInserts galaxyInserts;
    private final ResearchInserts researchInserts;
    private final SolarSystemInserts solarSystemInserts;

    @PostConstruct
    public void initialInserts() {
        galaxyAndPlanetInserts();
    }

    private void galaxyAndPlanetInserts() {

        planetInserts.getDataStream().log()
                .flatMap(this::connectPlanetData)
                .doOnComplete(() -> galaxyInserts.getDataStream().flatMap(this::connectGalaxyData).subscribe())
                .doOnComplete(solarSystemInserts::insertData)
                .doOnComplete(researchInserts::insertData)
                .subscribe();
    }

    private Publisher<Planet> connectPlanetData(Planet planet) {
        Arrays.stream(solarSystemInserts.getData()).filter(ss -> ss.getId().equals(planet.getSolarSystemId()))
                .findFirst().ifPresent(solarSystem -> solarSystem.getPlanetsCoordinatesIds().put(planet.getCoordinates(), planet.getId()));
        Arrays.stream(researchInserts.getData()).filter(research -> research.getId().equals(planet.getUserId()))
                .findFirst().ifPresent(research -> planet.setResearchId(research.getId()));
        return planetInserts.getRepository().save(planet);
    }

    private Publisher<Galaxy> connectGalaxyData(Galaxy galaxy) {
        galaxy.setSolarSystems(Arrays.stream(solarSystemInserts.getData()).map(SolarSystem::getId).collect(Collectors.toList()));
        return galaxyInserts.getRepository().save(galaxy);
    }
}
