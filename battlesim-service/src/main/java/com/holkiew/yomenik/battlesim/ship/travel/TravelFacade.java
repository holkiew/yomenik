package com.holkiew.yomenik.battlesim.ship.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelFacade {
    private final TravelService travelService;
}
