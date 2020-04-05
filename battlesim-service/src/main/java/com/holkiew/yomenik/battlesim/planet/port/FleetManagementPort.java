package com.holkiew.yomenik.battlesim.planet.port;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import reactor.core.publisher.Mono;

public interface FleetManagementPort {

    Mono<FleetManagementConfig> findById(String userId);

}
