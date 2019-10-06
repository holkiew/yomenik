package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.PlanetFacade;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import com.holkiew.yomenik.battlesim.ship.travel.dto.MoveShipRequest;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TravelService {

    private final PlanetFacade planetFacade;
    private final static Long travelTimeSeconds = 15L;

    public Mono<Boolean> moveShips(MoveShipRequest request, Principal principal) {
        return planetFacade.findByIdAndUserId(request.getPlanetIdFrom(), principal.getId())
                .map(this::updatePlanetResidingShips)
                .filter(planetHasRequestedShips(request))
                .zipWith(planetFacade.findById(request.getPlanetIdTo()))
                .map(sendShipsOnRoute(request))
                .flatMapMany(planetTuple -> planetFacade.saveAll(Flux.just(planetTuple.getT1(), planetTuple.getT2())))
                .hasElements();
        // TODO return arrival time?
    }

    Planet updatePlanetResidingShips(Planet planet) {
        List<Fleet> arrivedFleets = planet.getOnRouteFleets().stream()
                .filter(fleet -> !fleet.isOnRoute() && fleet.getPlanetIdTo().equals(planet.getId()))
                .peek((fleet) -> fleet.getShips().forEach((key, value) -> planet.getResidingFleet().merge(key, value, Long::sum)))
                .collect(Collectors.toList());
        planet.getOnRouteFleets().removeAll(arrivedFleets);
        return planet;
    }

    private Predicate<Planet> planetHasRequestedShips(MoveShipRequest request) {
        return planet -> {
            var requestedFleet = request.getFleet();
            return requestedFleet.entrySet().stream()
                    .allMatch(requestedShipEntry -> {
                        Long residingFleetAmount = planet.getResidingFleet().get(requestedShipEntry.getKey());
                        return Objects.nonNull(residingFleetAmount) && requestedShipEntry.getValue() <= residingFleetAmount;
                    });
        };
    }

    private Function<Tuple2<Planet, Planet>, Tuple2<Planet, Planet>> sendShipsOnRoute(MoveShipRequest request) {
        return planetsTuple -> {
            Fleet fleetOnRoute = subtractFleetFromFromPlanet(request, planetsTuple).get();
            Planet planetFrom = planetsTuple.getT1();
            Planet planetTo = planetsTuple.getT2();
            planetFrom.getOnRouteFleets().add(fleetOnRoute);
            planetTo.getOnRouteFleets().add(fleetOnRoute);
            fleetOnRoute.setRoute(planetTo.getId(), planetFrom.getId(), LocalDateTime.now().plusSeconds(travelTimeSeconds));
            return planetsTuple;
        };
    }

    private Optional<Fleet> subtractFleetFromFromPlanet(MoveShipRequest request, Tuple2<Planet, Planet> planetsTuple) {
        return request.getFleet().entrySet().stream().map((entry) -> {
            ShipType requestedShipType = entry.getKey();
            Long requestedShipAmount = entry.getValue();

            Planet planetFrom = planetsTuple.getT1();
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
