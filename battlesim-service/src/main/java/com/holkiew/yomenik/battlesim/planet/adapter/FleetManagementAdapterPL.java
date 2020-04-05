package com.holkiew.yomenik.battlesim.planet.adapter;

import com.holkiew.yomenik.battlesim.planet.port.FleetManagementPort;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.FleetManagementFacade;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FleetManagementAdapterPL implements FleetManagementPort {
    private final FleetManagementFacade fleetManagementFacade;

    public Mono<FleetManagementConfig> findById(String userId) {
        return fleetManagementFacade.findById(userId);
    }
}
