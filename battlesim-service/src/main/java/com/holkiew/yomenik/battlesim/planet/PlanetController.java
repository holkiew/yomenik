package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.entity.BuildingConfiguration;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.planet.model.request.DowngradeBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewResearchRequest;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.planet.model.response.dto.PlanetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@CrossOrigin

@RestController
@RequiredArgsConstructor
@RequestMapping("/planet")
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

    @GetMapping("planet")
    public Mono<ResponseEntity<Planet>> getPlanet(@RequestParam @NotBlank String planetId, @RequestParam(required = false) boolean asOwner, Principal principal) {
        return planetService.getPlanet(planetId, asOwner, principal)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/planets")
    public Mono<ResponseEntity<List<PlanetDTO>>> getPlanetsData(Principal principal) {
        return planetService.getPlanetsData(principal)
                .collectList()
                .transform(planetService::toPlanetDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/research")
    public Mono<ResponseEntity<Object>> newResearch(@RequestBody @Valid NewResearchRequest request, Principal principal) {
        return planetService.newResearch(request, principal)
                .map(tuple -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/configuration")
    public Mono<ResponseEntity<BuildingConfiguration>> getBuildingsConfiguration() {
        return planetService.getBuildingConfiguration()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
