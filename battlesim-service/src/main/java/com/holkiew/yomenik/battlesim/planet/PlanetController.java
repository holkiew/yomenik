package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.model.request.DowngradeBuildingRequest;
import com.holkiew.yomenik.battlesim.planet.model.request.NewBuildingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PlanetController {

    private final PlanetService planetService;

    @PostMapping("/building")

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


}
