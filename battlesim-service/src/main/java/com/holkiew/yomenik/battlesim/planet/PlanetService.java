package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import com.holkiew.yomenik.battlesim.planet.model.building.IronMine;
import com.holkiew.yomenik.battlesim.planet.model.exception.NotEnoughResourcesException;
import com.holkiew.yomenik.battlesim.planet.model.request.DowngradeBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.planet.port.PlanetRepository;
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

    public Mono<Resources> getPlanetResources(String planetId, Principal principal) {
        return planetRepository.findByIdAndUserId(planetId, principal.getId())
                .map(this::updatePlanetResourcesByIncome)
                .flatMap(planetRepository::save)
                .map(Planet::getResources);
    }

    public Mono<Planet> downgradeBuilding(DowngradeBuildingRequest request, Principal principal) {
        return planetRepository.findByIdAndUserId(request.getPlanetId(), principal.getId())
                .map(this::updatePlanetResourcesByIncome)
                .flatMap(planet -> downgradeBuilding(request, planet))
                .cast(Planet.class)
                .flatMap(planetRepository::save);
    }

    public Mono<Planet> createOrUpgradeBuilding(NewBuildingRequest request, Principal principal) {
        return planetRepository.findByIdAndUserId(request.getPlanetId(), principal.getId())
                .map(this::updatePlanetResourcesByIncome)
                .map(planet -> createOrUpgradeBuilding(request, planet))
                .doOnError(log::error)
                .cast(Planet.class)
                .flatMap(planetRepository::save);
    }

    private Planet updatePlanetResourcesByIncome(Planet planet) {
        int incomePerHour = IronMine.BASE_INCOME;
        incomePerHour += planet.getBuildings().values().stream()
                .filter(building -> building.getBuildingType().equals(BuildingType.MINE))
                .mapToLong(building -> (long) (Math.pow(IronMine.BASE_INCOME_PER_LEVEL_INCREASE, building.getLevel()) * IronMine.BASE_INCOME))
                .sum();
        planet.getResources().getIron().updateAmountByIncome(incomePerHour);
        return planet;
    }

    private Object createOrUpgradeBuilding(NewBuildingRequest request, Planet planet) {
        Optional<Building> buildingOptional = getBuilding(planet, request.getSlot());
        try {
            if (buildingOptional.isPresent()) {
                upgradeBuilding(request, buildingOptional.get(), planet);
            } else {
                buildBuilding(request, planet);
            }
        } catch (NotEnoughResourcesException e) {
            return Mono.error(e);
        }
        return planet;
    }

    private Mono<Object> downgradeBuilding(DowngradeBuildingRequest request, Planet planet) {
        Optional<Building> buildingOptional = getBuilding(planet, request.getSlot());
        buildingOptional.ifPresent(building -> {
            int buildingLevel = building.getLevel();
            Resources levelCost = building.getLevelCost(buildingLevel);
            levelCost.divide(0.1);
            planet.getResources().add(levelCost);
            if (buildingLevel > 1) {
                building.setLevel(buildingLevel - 1);
            } else {
                planet.getBuildings().remove(request.getSlot());
            }
        });
        return buildingOptional.isPresent() ? Mono.just(planet) : Mono.empty();
    }

    private void buildBuilding(NewBuildingRequest request, Planet planet) throws NotEnoughResourcesException {
        int buildingNextLevel = 1;
        var building = Building.builder()
                .buildingType(request.getBuildingType())
                .slot(request.getSlot())
                .level(buildingNextLevel).build();
        Resources nextLevelCost = building.getLevelCost(buildingNextLevel);
        if (planet.getResources().hasMoreOrEqual(nextLevelCost)) {
            planet.getResources().subtract(nextLevelCost);
            planet.getBuildings().put(request.getSlot(), building);
        } else {
            throw new NotEnoughResourcesException();
        }

    }

    private void upgradeBuilding(NewBuildingRequest request, Building building, Planet planet) throws NotEnoughResourcesException {
        if (building.getBuildingType().equals(request.getBuildingType())) {
            int buildingNextLevel = building.getLevel() + 1;
            Resources nextLevelCost = building.getLevelCost(buildingNextLevel);
            if (planet.getResources().hasMoreOrEqual(nextLevelCost)) {
                planet.getResources().subtract(nextLevelCost);
                building.setLevel(buildingNextLevel);
            } else {
                throw new NotEnoughResourcesException();
            }
        } else {
            throw new RuntimeException("Wrong building type, POTENTIAL FLAW");
        }
    }

    private Optional<Building> getBuilding(Planet planet, int slot) {
        Map<Integer, Building> buildings = planet.getBuildings();
        return Optional.ofNullable(buildings.get(slot));
    }
}
