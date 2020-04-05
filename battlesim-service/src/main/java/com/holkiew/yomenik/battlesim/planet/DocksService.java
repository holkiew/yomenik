package com.holkiew.yomenik.battlesim.planet;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.model.response.AvailableShipsToBuildResponse;
import com.holkiew.yomenik.battlesim.planet.port.FleetManagementPort;
import com.holkiew.yomenik.battlesim.planet.port.PlanetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class DocksService {

    private final PlanetRepository planetRepository;
    private final FleetManagementPort fleetManagementService;

    public Mono<AvailableShipsToBuildResponse> getAvailableShipsToBuild(Principal principal, String planetId) {
        return planetRepository.findByIdAndUserId(planetId, principal.getId())
                .transform(planetMono -> fleetManagementService.findById(principal.getId()))
                .map(fleetManagementConfig -> AvailableShipsToBuildResponse.builder()
                        .availableTemplates(fleetManagementConfig.getShipGroupTemplates().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getShipClassType())))
                        .build()
                );
    }
}
