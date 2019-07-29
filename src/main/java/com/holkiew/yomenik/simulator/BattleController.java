package com.holkiew.yomenik.simulator;

import com.holkiew.yomenik.simulator.dto.NewBattleRequest;
import com.holkiew.yomenik.simulator.persistence.BattleHistory;
import com.holkiew.yomenik.simulator.persistence.BattleHistoryRepository;
import com.holkiew.yomenik.simulator.persistence.BattleRecap;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.rmi.activation.UnknownObjectException;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/battle")
@Log4j2
public class BattleController {

    private final SimulatorFacade simulatorFacade;
    private final BattleService battleService;

    @GetMapping("dupa")
    public String dupa(){
        return "OK";
    }

    @PostMapping("/newBattle")
    public ResponseEntity<?> newBattle(@RequestBody NewBattleRequest request) {
        battleService.newBattle(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/currentBattle", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<BattleHistory> getCurrentBattleHistory() {
        return battleService.getCurrentBattleHistory();
    }
}
