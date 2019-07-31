package com.holkiew.yomenik.simulator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.simulator.ships.Ship;
import com.holkiew.yomenik.simulator.ships.ShipName;
import lombok.Data;

import java.util.ArrayList;
import java.util.Random;

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
        ListMultimap<ShipName, Ship> a2DestroyedShips = ArrayListMultimap.create();
        ListMultimap<ShipName, Ship> army2ShipsCopy = ArrayListMultimap.create();
        army2ShipsCopy.putAll(army2.getShips());

        resolveFiring(random, army1.getShips(), army2.getShips(), a2DestroyedShips);
        resolveFiring(random, army2ShipsCopy, army1.getShips(), army1.getDestroyedShips());

        army2.getDestroyedShips().putAll(a2DestroyedShips);
        this.battleStage = army1.getShips().size() == 0 || army2.getShips().size() == 0 ? BattleStage.END : battleStage.nextStage();
    }

    //marks and gets rid of taken down ships, A1 shoots at A2
    private void resolveFiring(Random random, ListMultimap<ShipName, Ship> a1Ships, ListMultimap<ShipName, Ship> a2Ships, ListMultimap<ShipName, Ship> a2DestroyedShips) {
        var army2Ships = new ArrayList<>(a2Ships.entries());
        var iterator = a1Ships.values().iterator();

        while (iterator.hasNext() && !army2Ships.isEmpty()) {
            var a1Ship = iterator.next();
            int shipToShoot = random.nextInt(army2Ships.size());
            var hitShipEntry = army2Ships.get(shipToShoot);
            Ship hitShip = hitShipEntry.getValue();
            hitShip.takeHit(a1Ship.getDamage());
            if(!hitShip.getAlive()){
                a2DestroyedShips.put(hitShipEntry.getKey(), hitShip);
                army2Ships.remove(hitShipEntry);
                a2Ships.remove(hitShipEntry.getKey(), hitShip);
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
