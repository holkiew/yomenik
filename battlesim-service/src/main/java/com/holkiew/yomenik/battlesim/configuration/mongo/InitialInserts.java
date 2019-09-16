package com.holkiew.yomenik.battlesim.configuration.mongo;

import com.google.common.collect.Maps;
import com.holkiew.yomenik.battlesim.galaxy.entity.Galaxy;
import com.holkiew.yomenik.battlesim.galaxy.port.GalaxyRepository;
import com.holkiew.yomenik.battlesim.galaxy.port.PlanetRepository;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitialInserts {

    private final GalaxyRepository galaxyRepository;
    private final PlanetRepository planetRepository;

    @PostConstruct
    public void initialInserts() {
        Galaxy galaxy = new Galaxy();
        galaxy.setId(1);
        galaxy.setPlanetsCoordinates(Maps.newHashMap());

        Planet planet1 = new Planet("1", galaxy.getId(), Tuples.of(1, 1));
        Planet planet2 = new Planet(null, galaxy.getId(), Tuples.of(1, 2));
        Planet planet3 = new Planet("3", galaxy.getId(), Tuples.of(1, 3));
        Planet[] planets = {planet1, planet2, planet3};
        Flux.just(planets).log()
                .flatMap(planet ->
                {
                    galaxy.getPlanetsCoordinates().put(Galaxy.toPlanetCoordinatesString(planet.getCoordinateX(), planet.getCoordinateY()), planet.getId());
                    return planetRepository.save(planet);
                })
                .doOnComplete(() ->
                        saveGalaxy(galaxy).subscribe()
                )
                .subscribe();
        System.out.println();

    }

    private Mono<Object> saveGalaxy(Galaxy galaxy) {
        return Mono.just(galaxy).log()
                .flatMap(galaxyRepository::save);
    }
}
