package com.holkiew.yomenik.battlesim.ship.travel.adapter;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.BattleFacade;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.dto.NewBattleRequest;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.entity.BattleHistory;
import com.holkiew.yomenik.battlesim.ship.travel.port.BattleHistoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class BattleHistoryServiceAdapterTR implements BattleHistoryPort {

    private final BattleFacade battleFacade;

    public Mono<BattleHistory> newBattle(NewBattleRequest request) {
        return battleFacade.newBattle(request);
    }

    public Mono<BattleHistory> findById(String id) {
        return battleFacade.findById(id);
    }

}
