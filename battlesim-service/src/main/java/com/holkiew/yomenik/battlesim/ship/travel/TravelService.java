package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.PlanetFacade;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import com.holkiew.yomenik.battlesim.ship.travel.dto.ExecuteTravelMissionRequest;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import com.holkiew.yomenik.battlesim.ship.travel.port.FleetRepository;
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

    private final PlanetFacade planetFacade;
    private final FleetRepository fleetRepository;

    public final static Long TRAVEL_TIME_SECONDS = 15L;

    public Mono<LocalDateTime> executeTravelMission(ExecuteTravelMissionRequest request, Principal principal) {
        return planetFacade.findByIdAndUserId(request.getPlanetIdFrom(), principal.getId())
                .log()
                .filter(planetHasRequestedShips(request))
                .zipWith(planetFacade.findById(request.getPlanetIdTo()))
                .flatMap(sendShipsOnRoute(request))
                .map(Fleet::getArrivalTime);
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
            planetFrom.getOnRouteFleets().put(request.getMissonType(), fleetOnRoute.getId());
            planetTo.getOnRouteFleets().put(request.getMissonType(), fleetOnRoute.getId());
            fleetOnRoute.setRoute(planetTo.getId(), planetFrom.getId(), LocalDateTime.now().plusSeconds(TRAVEL_TIME_SECONDS), request.getMissonType());
            return planetFacade.saveAll(Flux.just(planetFrom, planetTo))
                    .next()
                    .flatMap(planet -> fleetRepository.save(fleetOnRoute));
        };
    }

    private Optional<Fleet> subtractFleetFromFromPlanet(ExecuteTravelMissionRequest request, Planet planetFrom) {
        return request.getFleet().entrySet().stream().map((entry) -> {
            ShipType requestedShipType = entry.getKey();
            Long requestedShipAmount = entry.getValue();
            var residingFleetFrom = planetFrom.getResidingFleet();
            long remainingFleetAmount = residingFleetFrom.get(requestedShipType) - requestedShipAmount;
            residingFleetFrom.put(requestedShipType, remainingFleetAmount);
            HashMap<ShipType, Long> fleet = new HashMap<>();
            fleet.put(requestedShipType, requestedShipAmount);
            return fleet;
        }).reduce((map1, map2) -> {
            map1.putAll(map2);
            return map1;
        }).map(Fleet::new);
    }
}
