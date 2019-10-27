package com.holkiew.yomenik.battlesim.ship.fleetmanagement.port;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FleetManagementConfigRepository extends ReactiveMongoRepository<FleetManagementConfig, String> {
}
