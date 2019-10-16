package com.holkiew.yomenik.battlesim.configuration.mongo;

import com.google.common.collect.Lists;
import com.holkiew.yomenik.battlesim.configuration.mongo.insert.GalaxyInserts;
import com.holkiew.yomenik.battlesim.configuration.mongo.insert.PlanetInserts;
import com.holkiew.yomenik.battlesim.configuration.mongo.insert.SolarSystemInserts;
import com.holkiew.yomenik.battlesim.galaxy.entity.Galaxy;
import com.holkiew.yomenik.battlesim.galaxy.entity.SolarSystem;
import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class InitialInserts {

    private final PlanetInserts planetInserts;
    private final GalaxyInserts galaxyInserts;
    private final SolarSystemInserts solarSystemInserts;

    @PostConstruct
    public void initialInserts() {
        galaxyAndPlanetInserts();
    }

    private void galaxyAndPlanetInserts() {
        Galaxy galaxy = new Galaxy();
        galaxy.setId(1);
        galaxy.setSolarSystems(Lists.newArrayList());
        Planet planet1 = Planet.builder()
                .id("1").userId("1").galaxyId(galaxy.getId()).coordinates(new Coordinates(1, 1))
                .solarSystemId("1")
                .residingFleet(new HashMap<>() {{
                    put(ShipType.SHIP_LEVEL1, 100L);
                    put(ShipType.SHIP_LEVEL3, 10L);
                }})
                .build();
        Planet planet2 = new Planet("2", "1", galaxy.getId(), Tuples.of(1, 2), "1");


        planetInserts.setData(planet1, planet2);
        galaxyInserts.setData(galaxy);

        planetInserts.getDataStream().log()
                .map(planet -> {
                    Arrays.stream(solarSystemInserts.getData()).filter(ss -> ss.getId().equals(planet.getSolarSystemId()))
                            .findFirst().ifPresent(solarSystem -> solarSystem.getPlanetsCoordinates().put(planet.getCoordinates(), planet.getId()));
                    return planet;
                })
                .doOnComplete(() -> galaxyInserts.getDataStream().flatMap(g -> {
                    g.setSolarSystems(Arrays.stream(solarSystemInserts.getData()).map(SolarSystem::getId).collect(Collectors.toList()));
                    return galaxyInserts.getRepository().save(g);
                }).subscribe())
                .doOnComplete(solarSystemInserts::insertData)
                .subscribe();
    }
}
