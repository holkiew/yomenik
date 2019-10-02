package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.PlanetFacade;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import com.holkiew.yomenik.battlesim.ship.travel.dto.MoveShipRequest;
import com.holkiew.yomenik.battlesim.ship.travel.model.Fleet;
import lombok.RequiredArgsConstructor;
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
public class TravelService {

    private final PlanetFacade planetFacade;
    private final static Long travelTimeSeconds = 15L;

    public Mono<Boolean> moveShips(MoveShipRequest request, Principal principal) {
        return planetFacade.findByIdAndUserId(request.getPlanetIdFrom(), principal.getId())
                .filter(planetHasRequestedShips(request))
                .zipWith(planetFacade.findById(request.getPlanetIdTo()))
                .map(transferShipsBetweenPlanets(request))
                .flatMapMany(planetTuple -> planetFacade.saveAll(Flux.just(planetTuple.getT1(), planetTuple.getT2())))
                .hasElements();
        // TODO return arrival time?
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

    private Function<Tuple2<Planet, Planet>, Tuple2<Planet, Planet>> transferShipsBetweenPlanets(MoveShipRequest request) {
        return planetsTuple -> {
            Fleet fleetOnRoute = subtractFleetFromFromPlanet(request, planetsTuple).get();
            Planet planetFrom = planetsTuple.getT1();
            Planet planetTo = planetsTuple.getT2();
            planetFrom.getOnRouteFleets().add(fleetOnRoute);
            planetTo.getOnRouteFleets().add(fleetOnRoute);
            fleetOnRoute.setRoute(planetFrom.getId(), planetTo.getId(), LocalDateTime.now().plusSeconds(travelTimeSeconds));
            // TODO:: skanowanie tych flot za kazdym pobraniem danych o flocie
            //residingFleetTo.put(requestedShip, residingFleetTo.getOrDefault(requestedShip, 0L) + requestedAmount);

            return planetsTuple;
        };
    }

    private Optional<Fleet> subtractFleetFromFromPlanet(MoveShipRequest request, Tuple2<Planet, Planet> planetsTuple) {
        return request.getFleet().entrySet().stream().map((entry) -> {
            ShipType requestedShipType = entry.getKey();
            Long requestedShipAmount = entry.getValue();

            Planet planetFrom = planetsTuple.getT1();
            var residingFleetFrom = planetFrom.getResidingFleet();

            long departingFleetAmount = residingFleetFrom.get(requestedShipType) - requestedShipAmount;
            residingFleetFrom.put(requestedShipType, departingFleetAmount);
            HashMap<ShipType, Long> fleet = new HashMap<>();
            fleet.put(requestedShipType, departingFleetAmount);
            return fleet;
        }).reduce((map1, map2) -> {
            map1.putAll(map2);
            return map1;
        }).map(Fleet::new);
    }

}
