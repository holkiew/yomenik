package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelFacade {
    private final TravelService travelService;

    public Planet updatePlanetResidingShips(Planet planet) {
        return travelService.updatePlanetResidingShips(planet);
    }
}
