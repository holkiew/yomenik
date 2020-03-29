package com.holkiew.yomenik.battlesim.ship.fleetmanagement;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.DeleteShipGroupTemplateRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.ModifyShipGroupTemplateRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.NewShipGroupTemplateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/fleet_management")
@RequiredArgsConstructor
@Log4j2
public class FleetManagementController {

    private final FleetManagementService fleetService;

    @GetMapping
    public Mono<ResponseEntity<FleetManagementConfig>> getFleetManagementConfig(Principal principal) {
        return fleetService.getManagementConfig(principal)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/ship_template")
    public Mono<ResponseEntity<FleetManagementConfig>> newTemplate(Principal principal, @RequestBody @Valid NewShipGroupTemplateRequest request) {
        return fleetService.newShipGroupTemplate(principal, request)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @PutMapping("/ship_template")
    public Mono<ResponseEntity<FleetManagementConfig>> modifyTemplate(Principal principal, @RequestBody @Valid ModifyShipGroupTemplateRequest request) {
        return fleetService.modifyShipGroupTemplate(principal, request)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/ship_template")
    public Mono<ResponseEntity<FleetManagementConfig>> deleteTemplate(Principal principal, @RequestBody @Valid DeleteShipGroupTemplateRequest request) {
        return fleetService.deleteShipGroupTemplate(principal, request)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }
}

