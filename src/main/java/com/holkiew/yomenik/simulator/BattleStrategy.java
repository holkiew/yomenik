package com.holkiew.yomenik.simulator;

import com.holkiew.yomenik.simulator.ships.Ship;
import com.holkiew.yomenik.simulator.ships.ShipLevel1;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class BattleStrategy {
    private Army army1;
    private Army army2;
    private BattleStage battleStage;

    private BattleStrategy(Army army1, Army army2, BattleStage battleStage) {
        this.army1 = army1;
        this.army2 = army2;
        this.battleStage = battleStage;
    }

    public static BattleStrategy of(Army army1, Army army2){
        return new BattleStrategy(army1, army2, BattleStage.NEW);
    }

    public void resolveRound(){
        Random random = new Random();
        var a2DestroyedShips = new ArrayList<Ship>();

        resolveFiring(random, army1.getShipsLevel1(), army2.getShipsLevel1(), a2DestroyedShips);
        List<Ship> preStageArmy2 = Stream.of(army2.getShipsLevel1(), a2DestroyedShips).flatMap(Collection::stream).collect(Collectors.toList());
        resolveFiring(random, preStageArmy2, army1.getShipsLevel1(), army1.getDestroyedShipsLevel1());

        army2.getDestroyedShipsLevel1().addAll(a2DestroyedShips);
        this.battleStage = army1.getShipsLevel1().size() == 0 || army2.getShipsLevel1().size() == 0 ? BattleStage.END : battleStage.nextStage();
    }

    //marks and gets rid of taken down ships, A1 shoots at A2
    private void resolveFiring(Random random, List<Ship> a1Ships, List<Ship> a2Ships, List<Ship> a2DestroyedShips) {
        var iterator = a1Ships.iterator();
        while(iterator.hasNext() && !a2Ships.isEmpty()){
            var ship = iterator.next();
            int shipToShoot = random.nextInt(a2Ships.size());
            Ship hitShip = a2Ships.get(shipToShoot)
                    .takeHit(ship.getDamage());
            if(!hitShip.getAlive()){
                a2DestroyedShips.add(hitShip);
                a2Ships.remove(shipToShoot);
            }
        }
    }

    public boolean isBattleEnded(){
        return battleStage == BattleStage.END;
    }

    @Override
    public String toString() {
        return "BattleStrategy{" +
                "\narmy1=" + army1 +
                "\narmy2=" + army2 +
                "\nbattleStage=" + battleStage +
                '}';
    }
}
