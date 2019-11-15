package com.holkiew.yomenik.battlesim.ship.fleetmanagement;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@CrossOrigin

@RestController
@RequestMapping("/fleet_management")
@RequiredArgsConstructor
@Log4j2
public class FleetManagementController {

    private final FleetManagementService fleetService;

//    @PostMapping("/fleetmanagement")
//    public Mono<ResponseEntity<FleetManagementConfig>> newConfig(@RequestBody @Valid NewFleetManagementRequest request) {
//        return fleetService.newConfig(request)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/fleetmanagement")
//    public Mono<ResponseEntity<FleetManagementConfig>> modifyConfig(@RequestBody @Valid ModifyFleetManagementRequest request) {
//        return fleetService.modifyConfig(request)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }

    @GetMapping
    public Mono<ResponseEntity<FleetManagementConfig>> getConfig(Principal principal) {
        return fleetService.getManagementConfig(principal)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

