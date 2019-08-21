package com.holkiew.yomenik.battlesim.simulator;

import com.holkiew.yomenik.battlesim.configuration.cloud.model.Principal;
import com.holkiew.yomenik.battlesim.simulator.dto.NewBattleRequest;
import com.holkiew.yomenik.battlesim.simulator.entity.BattleHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class BattleController {

    private final BattleService battleService;

    @PostMapping("/battle")
    public Mono<ResponseEntity<Object>> newBattle(@RequestBody @Valid NewBattleRequest request, Principal principal) {
        return battleService.newBattle(request, principal)
                .map(battleHistory -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/currentBattle", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<BattleHistory> getCurrentBattle(Principal principal) {
        return battleService.getCurrentBattle(principal);
    }

    @DeleteMapping("/currentBattle")
    public Mono<ResponseEntity<Object>> cancelCurrentBattle(Principal principal) {
        return battleService.cancelCurrentBattle(principal)
                .map(battleHistory -> ResponseEntity.ok().build())
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @GetMapping("/battleHistory")
    public Mono<Tuple2<List<BattleHistory>, Long>> getBattleHistory(Principal principal, Pageable pageable) {
        return battleService.getBattleHistory(principal, pageable);
    }
}

