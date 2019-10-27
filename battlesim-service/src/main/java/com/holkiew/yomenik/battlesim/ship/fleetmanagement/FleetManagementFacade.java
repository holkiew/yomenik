package com.holkiew.yomenik.battlesim.ship.fleetmanagement;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.ModifyFleetManagementRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.NewFleetManagementRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.port.FleetManagementConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FleetManagementFacade {
    private final FleetManagementService service;
    private final FleetManagementConfigRepository repository;

    public Mono<FleetManagementConfig> modifyOrSaveConfig(ModifyFleetManagementRequest request) {
        return service.modifyConfig(request);
    }

    public Mono<FleetManagementConfig> newConfig(NewFleetManagementRequest request) {
        return service.newConfig(request);
    }

    public Mono<FleetManagementConfig> findById(String userId) {
        return repository.findById(userId);
    }
}
