package com.holkiew.yomenik.simulator;

import com.holkiew.yomenik.simulator.dto.NewBattleRequest;
import com.holkiew.yomenik.simulator.persistence.BattleHistory;
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

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping()
@Log4j2
public class BattleController {

    private final SimulatorFacade simulatorFacade;
    private final BattleService battleService;

    @PostMapping("/newBattle")
    public ResponseEntity<?> newBattle(@RequestBody NewBattleRequest request) {
        battleService.newBattle(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/currentBattle", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<BattleHistory> getCurrentBattle() {
        return battleService.getCurrentBattle();
    }

    @GetMapping("/battleHistory")
    public Flux<BattleHistory> getBattleHistory(Pageable pageable) {
        return battleService.getBattleHistory(pageable);
    }

    @GetMapping("/dupa")
    public String getBattleHistory(Principal principal, ServerHttpRequest request) {
        System.out.println(principal.getName());
        log.error(request.getHeaders());
        return "dupa";
    }
}

