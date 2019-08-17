package com.holkiew.yomenik.battlesim.simulator;

import com.holkiew.yomenik.battlesim.simulator.dto.NewBattleRequest;
import com.holkiew.yomenik.battlesim.simulator.entity.BattleHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Log4j2
public class BattleController {

    private final BattleService battleService;

    @PostMapping("/newBattle")
    public Mono<ResponseEntity<Object>> newBattle(@RequestBody @Valid NewBattleRequest request, ServerHttpRequest serverRequest) {
        return battleService.newBattle(request, serverRequest)
                .map(battleHistory -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/currentBattle", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<BattleHistory> getCurrentBattle(ServerHttpRequest serverRequest) {
        return battleService.getCurrentBattle(serverRequest);
    }

    @DeleteMapping("/currentBattle")
    public Mono<ResponseEntity<Object>> cancelCurrentBattle(ServerHttpRequest serverRequest) {
        return battleService.cancelCurrentBattle(serverRequest)
                .map(battleHistory -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @GetMapping("/battleHistory")
    public Flux<BattleHistory> getBattleHistory(Pageable pageable) {
        return battleService.getBattleHistory(pageable);
    }
}

