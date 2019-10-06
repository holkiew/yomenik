package com.holkiew.yomenik.battlesim.configuration.mongo;

import com.google.common.collect.Maps;
import com.holkiew.yomenik.battlesim.configuration.mongo.insert.GalaxyInserts;
import com.holkiew.yomenik.battlesim.configuration.mongo.insert.PlanetInserts;
import com.holkiew.yomenik.battlesim.galaxy.entity.Galaxy;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class InitialInserts {

    private final PlanetInserts planetInserts;
    private final GalaxyInserts galaxyInserts;

    @PostConstruct
    public void initialInserts() {
        galaxyAndPlanetInserts();
    }

    private void galaxyAndPlanetInserts() {
        Galaxy galaxy = new Galaxy();
        galaxy.setId(1);
        galaxy.setPlanetsCoordinates(Maps.newHashMap());
        Planet planet1 = Planet.builder()
                .id("1").userId("1").galaxyId(galaxy.getId()).coordinateX(1).coordinateY(1)
                .residingFleet(new HashMap() {{
                    put(ShipType.SHIP_LEVEL1, 100L);
                    put(ShipType.SHIP_LEVEL3, 10L);
                }})
                .build();
        Planet planet2 = new Planet("2", "1", galaxy.getId(), Tuples.of(1, 2));
//        Planet planet3 = new Planet("3", "3", galaxy.getId(), Tuples.of(1, 3));

        planetInserts.setData(planet1, planet2
                //, planet3
        );
        galaxyInserts.setData(galaxy);
        planetInserts.getDataStream().log()
                .flatMap(planet ->
                {
                    galaxy.getPlanetsCoordinates().put(Galaxy.toPlanetCoordinatesString(planet.getCoordinateX(), planet.getCoordinateY()), planet.getId());
                    return planetInserts.getRepository().save(planet);
                })
                .doOnComplete(galaxyInserts::insertData)
                .subscribe();
    }
}
