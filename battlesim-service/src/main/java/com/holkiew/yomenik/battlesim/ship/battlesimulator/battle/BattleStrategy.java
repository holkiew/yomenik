package com.holkiew.yomenik.battlesim.ship.battlesimulator.battle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.Ship;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import lombok.Data;

import java.util.ArrayList;
import java.util.Random;

@Data
public class BattleStrategy {
    private Army army1;
    private Army army2;
    private BattleStage battleStage;

    private Random randomGenerator;

    private BattleStrategy(Army army1, Army army2, BattleStage battleStage) {
        this.army1 = army1;
        this.army2 = army2;
        this.battleStage = battleStage;
        this.randomGenerator = new Random();
    }

    public static BattleStrategy of(Army army1, Army army2) {
        return new BattleStrategy(army1, army2, BattleStage.NEW);
    }

    public void resolveRound() {
        var a2DestroyedShips = ArrayListMultimap.<ShipType, Ship>create();
        var army2ShipsCopy = ArrayListMultimap.<ShipType, Ship>create();
        army2ShipsCopy.putAll(army2.getShips());

        resolveFiring(army1.getShips(), army2.getShips(), a2DestroyedShips);
        resolveFiring(army2ShipsCopy, army1.getShips(), army1.getDestroyedShips());

        army2.getDestroyedShips().putAll(a2DestroyedShips);
        this.battleStage = army1.getShips().size() == 0 || army2.getShips().size() == 0 ? BattleStage.END : battleStage.nextStage();
    }

    public void resolveRetreatRound() {
        resolveFiring(army2.getShips(), army1.getShips(), army1.getDestroyedShips());
        this.battleStage = BattleStage.END;
    }

    //marks and gets rid of taken down ships, A1 shoots at A2
    private void resolveFiring(ListMultimap<ShipType, Ship> shootingShips, ListMultimap<ShipType, Ship> hitShips, ListMultimap<ShipType, Ship> hitDestroyedShips) {
        var army2Ships = new ArrayList<>(hitShips.entries());
        var iterator = shootingShips.values().iterator();

        while (iterator.hasNext() && !army2Ships.isEmpty()) {
            var shootingShip = iterator.next();
            int shipToShoot = randomGenerator.nextInt(army2Ships.size());
            var hitShipEntry = army2Ships.get(shipToShoot);
            Ship hitShip = hitShipEntry.getValue();
            for (Weapon shipShoot : shootingShip.getShipShoots()) {
                hitShip.takeHit(shipShoot);
                if (!hitShip.getAlive()) {
                    hitDestroyedShips.put(hitShipEntry.getKey(), hitShip);
                    army2Ships.remove(hitShipEntry);
                    hitShips.remove(hitShipEntry.getKey(), hitShip);
                    break;
                }
            }

        }
    }

    public boolean isBattleEnded() {
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
