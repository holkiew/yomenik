package com.holkiew.yomenik.battlesim.galaxy;

import com.holkiew.yomenik.battlesim.galaxy.entity.Galaxy;
import com.holkiew.yomenik.battlesim.galaxy.port.GalaxyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GalaxyService {

    private final GalaxyRepository galaxyRepository;

    public Mono<Galaxy> getGalaxy(int galaxyNum) {
        return galaxyRepository.findById(galaxyNum);
    }
}

