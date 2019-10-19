package com.holkiew.yomenik.battlesim.ship.battlesimulator;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.dto.NewBattleRequest;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.port.BattleHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BattleFacade {
    private final BattleService battleService;
    private final BattleHistoryRepository repository;

    public Mono<BattleHistory> newBattle(NewBattleRequest request) {
        return battleService.newBattle(request);
    }

    public Mono<BattleHistory> findById(String id) {
        return repository.findById(id);
    }
}
