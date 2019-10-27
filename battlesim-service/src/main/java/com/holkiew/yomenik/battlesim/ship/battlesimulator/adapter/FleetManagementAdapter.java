package com.holkiew.yomenik.battlesim.ship.battlesimulator.adapter;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.port.FleetManagementServicePort;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.FleetManagementFacade;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FleetManagementAdapter implements FleetManagementServicePort {
    private final FleetManagementFacade facade;

    public Mono<FleetManagementConfig> findById(String userId) {
        return facade.findById(userId);
    }
}
