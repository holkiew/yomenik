package com.holkiew.yomenik.battlesim.galaxy;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/galaxy")
@RequiredArgsConstructor
public class GalaxyController {

    private final GalaxyService galaxyService;

    @GetMapping
    public Mono<ResponseEntity<Object>> getGalaxy(@RequestParam int galaxyNum) {
        return galaxyService.getGalaxy(galaxyNum)
                .map(galaxy -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
