package com.holkiew.yomenik.battlesim.ship.fleetmanagement;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.ModifyFleetManagementRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.NewFleetManagementRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.port.FleetManagementConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.holkiew.yomenik.battlesim.common.util.MethodUtils.updateValue;


@Service
@RequiredArgsConstructor
@Log4j2
public class FleetManagementService {

    private final FleetManagementConfigRepository managementRepository;

    public Mono<FleetManagementConfig> modifyConfig(ModifyFleetManagementRequest request) {
        return managementRepository.findById(request.getUserId())
                .map(config -> {
                    updateValue(config::setFireMode, request.getFireMode());
                    return config;
                }).flatMap(managementRepository::save);
    }

    public Mono<FleetManagementConfig> newConfig(NewFleetManagementRequest request) {
        var newConfig = FleetManagementConfig.builder()
                .id(request.getUserId()).fireMode(request.getFireMode()).build();
        return managementRepository.save(newConfig);
    }
}
