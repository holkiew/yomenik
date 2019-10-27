package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.model.request.DowngradeBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewResearchRequest;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
public class PlanetController {

    private final PlanetService planetService;

    @PutMapping("/building")
    public Mono<ResponseEntity<Object>> createOrUpgradeBuilding(@RequestBody @Valid NewBuildingRequest request, Principal principal) {
        return planetService.createOrUpgradeBuilding(request, principal)
                .map(planet -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/building")
    public Mono<ResponseEntity<Object>> downgradeBuilding(@RequestBody @Valid DowngradeBuildingRequest request, Principal principal) {
        return planetService.downgradeBuilding(request, principal)
                .map(planet -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/resource")
    public Mono<ResponseEntity<Resources>> getPlanetResources(@RequestParam @NotBlank String planetId, Principal principal) {
        return planetService.getPlanetResources(planetId, principal)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/research")
    public Mono<ResponseEntity<Object>> newResearch(@RequestBody @Valid NewResearchRequest request, Principal principal) {
        return planetService.newResearch(request, principal)
                .map(tuple -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
