package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.travel.dto.ExecuteTravelMissionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDateTime;

@CrossOrigin

@RestController
@RequiredArgsConstructor
@RequestMapping("travel")
@Log4j2
public class TravelController {

    private final TravelService travelService;

    @PostMapping
    public Mono<ResponseEntity<LocalDateTime>> executeTravelMission(@RequestBody @Valid ExecuteTravelMissionRequest request, Principal principal) {
        return travelService.executeTravelMission(request, principal)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/event/battleEndDateChange")
    public Mono<ResponseEntity<Object>> battleEndDateChangeEvent(@RequestBody BattleHistory battleHistory) {
        return travelService.battleEndDateChangeEvent(battleHistory)
                .map(fleet -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

