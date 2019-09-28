package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import com.holkiew.yomenik.battlesim.planet.model.building.IronMine;
import com.holkiew.yomenik.battlesim.planet.model.request.DowngradeBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.planet.port.PlanetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlanetService {

    private final PlanetRepository planetRepository;

    public Mono<Resources> getPlanetResources(String planetId, Principal principal) {
        return planetRepository.findByIdAndUserId(planetId, principal.getId())
                .map(planet -> {
                    // TODO::: base_income.. + other resources
                    int incomePerHour = IronMine.BASE_INCOME;
                    incomePerHour += planet.getBuildings().values().stream()
                            .filter(building -> building.getBuildingType().equals(BuildingType.MINE))
                            .mapToInt(building -> (int) (building.getLevel() * IronMine.PER_LEVEL_INCREASE * IronMine.BASE_INCOME))
                            .sum();
                    planet.getResources().getIron().updateAmountByIncome(incomePerHour);
                    return planet;
                })
                .flatMap(planetRepository::save)
                .map(Planet::getResources);
    }

    public Mono<Planet> downgradeBuilding(DowngradeBuildingRequest request, Principal principal) {
        return planetRepository.findByIdAndUserId(request.getPlanetId(), principal.getId())
                .flatMap(planet -> downgradeBuilding(request, planet))
                .cast(Planet.class)
                .flatMap(planetRepository::save);
    }

    public Mono<Planet> createOrUpgradeBuilding(NewBuildingRequest request, Principal principal) {
        // TODO if building is resource type, then update resources to avoid surplus
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
            if (Objects.nonNull(error)) return error;
        } else {
            buildBuilding(request, planet);
        }
        return planet;
    }

    private void buildBuilding(NewBuildingRequest request, Planet planet) {
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

    private Mono<Object> downgradeBuilding(DowngradeBuildingRequest request, Planet planet) {
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
        return buildingOptional.isPresent() ? Mono.just(planet) : Mono.empty();
    }

    private Optional<Building> getBuilding(Planet planet, int slot) {
        Map<Integer, Building> buildings = planet.getBuildings();
        return Optional.ofNullable(buildings.get(slot));
    }
}
