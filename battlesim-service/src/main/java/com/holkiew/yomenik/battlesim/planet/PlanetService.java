package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.entity.Building;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.entity.Research;
import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import com.holkiew.yomenik.battlesim.planet.model.building.properties.IronMineProperties;
import com.holkiew.yomenik.battlesim.planet.model.exception.NotEnoughResourcesException;
import com.holkiew.yomenik.battlesim.planet.model.request.DowngradeBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewResearchRequest;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.planet.model.response.dto.PlanetDTO;
import com.holkiew.yomenik.battlesim.planet.model.response.dto.PlanetMapper;
import com.holkiew.yomenik.battlesim.planet.port.FleetPort;
import com.holkiew.yomenik.battlesim.planet.port.PlanetRepository;
import com.holkiew.yomenik.battlesim.planet.port.ResearchRepository;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final ResearchRepository researchRepository;
    private final FleetPort fleetPort;
    private final PlanetMapper planetMapper;

    public Mono<Planet> getPlanet(String planetId, boolean asOwner, Principal principal) {
        if (asOwner) {
            return planetRepository.findByIdAndUserId(planetId, principal.getId())
                    .transform(this::updatePlanetResourcesByIncomeAndSave);

        } else {
            return planetRepository.findById(planetId)
                    .transform(this::updatePlanetResourcesByIncomeAndSave)
                    .doOnEach(planet -> {
                        planet.get().getOnRouteFleets().clear();
                        planet.get().setUserId("");
                    });
        }
    }

    public Flux<Planet> getPlanetsData(Principal principal) {
        return planetRepository.findAllByUserId(principal.getId())
                .map(this::updatePlanetResourcesByIncome)
                .flatMap(planetRepository::save);
    }

    public Mono<Resources> getPlanetResources(String planetId, Principal principal) {
        return planetRepository.findByIdAndUserId(planetId, principal.getId())
                .transform(this::updatePlanetResourcesByIncomeAndSave)
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

    public Mono<Tuple2<Planet, Research>> newResearch(NewResearchRequest request, Principal principal) {
        return planetRepository.findById(request.getPlanetId())
                .map(this::updatePlanetResourcesByIncome)
                .zipWith(researchRepository.findById(principal.getId()))
                .flatMap(upgradeResearch(request))
                .flatMap(tuple -> planetRepository.save(tuple.getT1()).zipWith(researchRepository.save(tuple.getT2())));

    }


    public Mono<List<PlanetDTO>> toPlanetDTO(Mono<List<Planet>> planetsMono) {
        return planetsMono.flatMap(planetCollection -> {
            var fleetIdsSet = planetCollection.stream().flatMap(planet -> planet.getOnRouteFleets().values().stream()).collect(Collectors.toSet());
            return Mono.just(planetCollection).zipWith(fleetPort.findByIds(fleetIdsSet).collect(Collectors.toSet()));
        })
                .map(planetsAndFleets -> {
                    Collection<Planet> planets = planetsAndFleets.getT1();
                    Set<Fleet> fleets = planetsAndFleets.getT2();
                    return planetMapper.mapToDTOList(planets, fleets);
                });
    }

    private Function<Tuple2<Planet, Research>, Mono<Tuple2<Planet, Research>>> upgradeResearch(NewResearchRequest request) {
        return tuple -> {
            Planet planet = tuple.getT1();
            var userResearchLevels = tuple.getT2().getResearchLevels();
            int nextLevel = userResearchLevels.get(request.getResearchType()) + 1;
            var nextLevelCost = request.getResearchType().getProperties().getCostByLevel(nextLevel);
            if (planet.getResources().hasMoreOrEqual(nextLevelCost)) {
                planet.getResources().subtract(nextLevelCost);
                userResearchLevels.put(request.getResearchType(), nextLevel);
            } else {
                return Mono.empty();
            }
            return Mono.just(tuple);
        };
    }

    private Planet updatePlanetResourcesByIncome(Planet planet) {
        var properties = (IronMineProperties) BuildingType.IRON_MINE.getProperties();
        int incomePerHour = properties.baseIncome;
        incomePerHour += planet.getBuildings().values().stream()
                .filter(building -> building.getBuildingType().equals(BuildingType.IRON_MINE))
                .mapToLong(building -> (long) (Math.pow(properties.baseCostIncreasePerLevel, building.getLevel()) * properties.baseIncome))
                .sum();
        var planetIron = planet.getResources().getIron();
        planetIron.updateAmountByIncome(incomePerHour);
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
            Resources levelCost = building.getBuildingType().getProperties().getLevelCost(buildingLevel);
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
        Resources nextLevelCost = building.getBuildingType().getProperties().getLevelCost(buildingNextLevel);
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
            Resources nextLevelCost = building.getBuildingType().getProperties().getLevelCost(buildingNextLevel);
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

    private Mono<Planet> updatePlanetResourcesByIncomeAndSave(Mono<Planet> stream) {
        return stream.map(this::updatePlanetResourcesByIncome)
                .flatMap(planetRepository::save);
    }

}
