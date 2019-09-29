package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.ship.travel.dto.MoveShipRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Log4j2
public class TravelController {

    private final TravelService travelService;

    @PutMapping("/shiptravel")
    public Mono<ResponseEntity<Object>> moveShips(@RequestBody @Valid MoveShipRequest request, Principal principal) {
        return travelService.moveShips(request, principal)
                .map(didMove -> didMove ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

