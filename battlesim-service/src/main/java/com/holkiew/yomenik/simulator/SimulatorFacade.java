package com.holkiew.yomenik.simulator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class SimulatorFacade {

    private final BattleService battleService;

////    @PostConstruct
//    public void newBattle(){
//        int healthPoints = 100, damage = 25;
//        int a1nShips = 10, a2nShips = 5;
//        var ships1 = Stream.generate(() -> (Ship)new ShipLevel1(healthPoints, damage))
//                .limit(a1nShips).collect(Collectors.toCollection(ArrayList::new));
//        var ships2 = Stream.generate(() -> (Ship)new ShipLevel1(healthPoints, damage))
//                .limit(a2nShips).collect(Collectors.toCollection(ArrayList::new));
//        Army army1 = new Army(ships1), army2 = new Army(ships2);
//        battleService.resolveBattle(army1, army2);
//    }
}
