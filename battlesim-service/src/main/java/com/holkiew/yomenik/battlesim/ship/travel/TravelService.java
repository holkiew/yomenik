package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.travel.dto.ExecuteTravelMissionRequest;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import com.holkiew.yomenik.battlesim.ship.travel.port.FleetRepository;
import com.holkiew.yomenik.battlesim.ship.travel.port.PlanetPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


@Service
@RequiredArgsConstructor
@Log4j2
public class TravelService {

    private final PlanetPort planetService;
    private final FleetRepository fleetRepository;

    private final int travelTimeMultiplier = 999;

    public Mono<LocalDateTime> executeTravelMission(ExecuteTravelMissionRequest request, Principal principal) {
        return planetService.findByIdAndUserId(request.getPlanetIdFrom(), principal.getId())
                .filter(planetHasRequestedShips(request))
                .zipWith(planetService.findById(request.getPlanetIdTo()))
                .flatMap(sendShipsOnRoute(request))
                .map(Fleet::getArrivalTime);
    }

    public Mono<Fleet> battleEndDateChangeEvent(BattleHistory battleHistory) {
        return fleetRepository.findByRelatedBattleHistoryId(battleHistory.getId())
                .map(fleet -> {
                    fleet.setArrivalTime(battleHistory.getEndDate());
                    return fleet;
                })
                .flatMap(fleetRepository::save);
    }

    private Predicate<Planet> planetHasRequestedShips(ExecuteTravelMissionRequest request) {
        return planet -> {
            var requestedFleet = request.getFleet();
            return requestedFleet.entrySet().stream()
                    .allMatch(requestedShipEntry -> {
                        Long residingFleetAmount = planet.getResidingFleet().get(requestedShipEntry.getKey());
                        return Objects.nonNull(residingFleetAmount) && requestedShipEntry.getValue() <= residingFleetAmount;
                    });
        };
    }

    private Function<Tuple2<Planet, Planet>, Mono<Fleet>> sendShipsOnRoute(ExecuteTravelMissionRequest request) {
        return planetsTuple -> {
            Planet planetFrom = planetsTuple.getT1();
            Planet planetTo = planetsTuple.getT2();
            Fleet fleetOnRoute = subtractFleetFromFromPlanet(request, planetFrom).get();
            planetFrom.getOnRouteFleets().put(request.getMissionType(), fleetOnRoute.getId());
            planetTo.getOnRouteFleets().put(request.getMissionType(), fleetOnRoute.getId());
            fleetOnRoute.setRoute(planetTo.getId(), planetFrom.getId(), getArrivalTime(planetFrom, planetTo), request.getMissionType());
            return planetService.saveAll(Flux.just(planetFrom, planetTo))
                    .next()
                    .flatMap(planet -> fleetRepository.save(fleetOnRoute));
        };
    }

    private Optional<Fleet> subtractFleetFromFromPlanet(ExecuteTravelMissionRequest request, Planet planetFrom) {
        return request.getFleet().entrySet().stream().map((entry) -> {
            String requestedShipTemplateName = entry.getKey();
            Long requestedShipAmount = entry.getValue();
            var residingFleetFrom = planetFrom.getResidingFleet();
            long remainingFleetAmount = residingFleetFrom.get(requestedShipTemplateName) - requestedShipAmount;
            residingFleetFrom.put(requestedShipTemplateName, remainingFleetAmount);
            var fleet = new HashMap<String, Long>();
            fleet.put(requestedShipTemplateName, requestedShipAmount);
            return fleet;
        }).reduce((map1, map2) -> {
            map1.putAll(map2);
            return map1;
        }).map(Fleet::new);
    }

    private LocalDateTime getArrivalTime(Planet planetFrom, Planet planetTo) {
        if (planetFrom.getSolarSystemId().equals(planetTo.getSolarSystemId())) {
            double distance = Math.sqrt(Math.pow(planetTo.getCoordinates().x - planetFrom.getCoordinates().x, 2) + Math.pow(planetTo.getCoordinates().y - planetFrom.getCoordinates().y, 2)) * travelTimeMultiplier;
            return LocalDateTime.now().plusSeconds((int) distance);
        } else {
            double distance = Math.sqrt(Math.pow(planetTo.getCoordinates().x - planetFrom.getCoordinates().x, 2) + Math.pow(planetTo.getCoordinates().y - planetFrom.getCoordinates().y, 2)) * travelTimeMultiplier * 3;
            return LocalDateTime.now().plusSeconds((int) distance);
        }
    }
}
