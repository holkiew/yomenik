package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.PlanetFacade;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.travel.dto.MoveShipRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;


@Service
@RequiredArgsConstructor
public class TravelService {

    private final PlanetFacade planetFacade;


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
            request.getFleet().forEach((requestedShip, requestedAmount) -> {
                var residingFleetFrom = planetsTuple.getT1().getResidingFleet();
                var residingFleetTo = planetsTuple.getT2().getResidingFleet();
                // TODO : mission types
                residingFleetFrom.put(requestedShip, residingFleetFrom.get(requestedShip) - requestedAmount);
                residingFleetTo.put(requestedShip, residingFleetTo.getOrDefault(requestedShip, 0L) + requestedAmount);
            });
            return planetsTuple;
        };
    }

}
