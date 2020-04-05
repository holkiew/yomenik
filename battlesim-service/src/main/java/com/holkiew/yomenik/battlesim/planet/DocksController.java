package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.model.request.AddNewShipsToBuildQueueRequest;
import com.holkiew.yomenik.battlesim.planet.model.response.AvailableShipsToBuildResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/docks")
@RequiredArgsConstructor
public class DocksController {

    private final DocksService docksService;

    @GetMapping
    public Mono<ResponseEntity<AvailableShipsToBuildResponse>> getAvailableShipsToBuild(Principal principal, @RequestParam String planetId) {
        return docksService.getAvailableShipsToBuild(principal, planetId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping
    public void addNewShipsToBuildQueue(Principal principal, AddNewShipsToBuildQueueRequest request) {

    }


}
