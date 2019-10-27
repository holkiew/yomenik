package com.holkiew.yomenik.battlesim.ship.fleetmanagement;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.ModifyFleetManagementRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.NewFleetManagementRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Log4j2
public class FleetManagementController {

    private final FleetManagementService fleetService;

    @PostMapping("/fleetmanagement")
    public Mono<ResponseEntity<FleetManagementConfig>> newConfig(@RequestBody @Valid NewFleetManagementRequest request) {
        return fleetService.newConfig(request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/fleetmanagement")
    public Mono<ResponseEntity<FleetManagementConfig>> modifyConfig(@RequestBody @Valid ModifyFleetManagementRequest request) {
        return fleetService.modifyConfig(request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

