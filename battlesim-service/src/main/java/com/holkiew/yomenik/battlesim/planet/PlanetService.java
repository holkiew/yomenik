package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.galaxy.port.PlanetRepository;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.model.request.DowngradeBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewBuildingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlanetService {

    private final PlanetRepository planetRepository;

    public Mono<Planet> downgradeBuilding(DowngradeBuildingRequest request, Principal principal) {
        return planetRepository.findByIdAndUserId(request.getPlanetId(), principal.getId())
                .map(planet -> downgradeBuilding(request, planet))
                .cast(Planet.class)
                .flatMap(planetRepository::save);
    }

    public Mono<Planet> createOrUpgradeBuilding(NewBuildingRequest request, Principal principal) {
        return planetRepository.findByIdAndUserId(request.getPlanetId(), principal.getId())
                .map(planet -> createOrUpgradeBuilding(request, planet))
                .doOnError(log::error)
                .cast(Planet.class)
                .flatMap(planetRepository::save);
    }

    private Object createOrUpgradeBuilding(NewBuildingRequest request, Planet planet) {
        Optional<Building> buildingOptional = getBuilding(planet, request.getSlot());
        if (buildingOptional.isPresent()) {
            Object error = upgradeBuilding(request, buildingOptional.get());
            if (error != null) return error;
        } else {
            buildBuilding(request, planet);
        }
        return planet;
    }

    private void buildBuilding(NewBuildingRequest request, Planet planet) {
        // TODO:: validator, if enough resources then level up
        var building = Building.builder()
                .buildingType(request.getBuildingType())
                .slot(request.getSlot())
                .level(1).build();
        planet.getBuildings().put(request.getSlot(), building);
    }

    private Object upgradeBuilding(NewBuildingRequest request, Building building) {
        if (building.getBuildingType().equals(request.getBuildingType())) {
            // TODO:: validator, if enough resources then level up
            building.setLevel(building.getLevel() + 1);
        } else {
            return Mono.error(new RuntimeException("Wrong building type, POTENTIAL FLAW"));
        }
        return null;
    }

    private Object downgradeBuilding(DowngradeBuildingRequest request, Planet planet) {
        Optional<Building> buildingOptional = getBuilding(planet, request.getSlot());
        buildingOptional.ifPresent(building -> {
            // TODO: requirements, resources back?
            int level = building.getLevel();
            if (level > 1) {
                building.setLevel(level - 1);
            } else {
                planet.getBuildings().remove(request.getSlot());
            }
        });
        return buildingOptional.isPresent() ? planet : Mono.empty();
    }

    private Optional<Building> getBuilding(Planet planet, int slot) {
        Map<Integer, Building> buildings = planet.getBuildings();
        return Optional.ofNullable(buildings.get(slot));
    }
}
