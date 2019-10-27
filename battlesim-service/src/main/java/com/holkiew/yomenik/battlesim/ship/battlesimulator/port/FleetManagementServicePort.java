package com.holkiew.yomenik.battlesim.ship.battlesimulator.port;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import reactor.core.publisher.Mono;

public interface FleetManagementServicePort {
    Mono<FleetManagementConfig> findById(String userId);
}
