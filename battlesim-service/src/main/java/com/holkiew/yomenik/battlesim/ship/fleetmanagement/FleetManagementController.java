package com.holkiew.yomenik.battlesim.ship.fleetmanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}

