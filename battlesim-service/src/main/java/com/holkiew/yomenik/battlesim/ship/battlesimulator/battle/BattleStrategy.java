package com.holkiew.yomenik.battlesim.ship.battlesimulator.battle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.battlesim.common.model.ShipClassType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.Ship;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.ShipGroupTemplate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

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
        var a2DestroyedShips = ArrayListMultimap.<ShipClassType, Ship>create();
        var army2ShipsCopy = ArrayListMultimap.<ShipClassType, Ship>create();
        army2ShipsCopy.putAll(army2.getShips());

        resolveFiring(army1.getShips(), army1.getFleetManagementConfig(), army2.getShips(), a2DestroyedShips);
        resolveFiring(army2ShipsCopy, army2.getFleetManagementConfig(), army1.getShips(), army1.getDestroyedShips());

        army2.getDestroyedShips().putAll(a2DestroyedShips);
        this.battleStage = army1.getShips().size() == 0 || army2.getShips().size() == 0 ? BattleStage.END : battleStage.nextStage();
    }

    public void resolveRetreatRound() {
        resolveFiring(army2.getShips(), army2.getFleetManagementConfig(), army1.getShips(), army1.getDestroyedShips());
        this.battleStage = BattleStage.END;
    }

    private void resolveFiring(ListMultimap<ShipClassType, Ship> shootingShips, FleetManagementConfig shootingShipsConfig, ListMultimap<ShipClassType, Ship> hitShips, ListMultimap<ShipClassType, Ship> hitDestroyedShips) {
        var defendingShips = new ArrayList<>(hitShips.values());
        var iterator = shootingShips.values().iterator();
        var shipGroupTemplates = shootingShipsConfig.getShipGroupTemplates();

        while (iterator.hasNext() && !defendingShips.isEmpty()) {
            var shootingShip = iterator.next();
            var shootingShipConfig = shipGroupTemplates.get(shootingShip.getFleetGroupTemplateName());
            switch (shootingShipConfig.getFireMode()) {
                case FIXED:
                    resolveFixedShooting(hitShips, hitDestroyedShips, defendingShips, shootingShipConfig);
                    break;
                case SCATTER:
                    resolveScatterShooting(hitShips, hitDestroyedShips, defendingShips, shootingShipConfig);
                    break;
            }
        }
    }

    private void resolveFixedShooting(ListMultimap<ShipClassType, Ship> hitShips, ListMultimap<ShipClassType, Ship> hitDestroyedShips, ArrayList<Ship> defendingShips, ShipGroupTemplate shootingShipConfig) {

        Ship hitShip;
        hitShip = drawShipToShoot(defendingShips);
        for (Weapon shipShoot : shootingShipConfig.getWeaponSlots().values().stream().filter(Objects::nonNull).collect(Collectors.toList())) {
            hitShip.takeHit(shipShoot);
            if (!hitShip.getAlive()) {
                hitDestroyedShips.put(hitShip.getShipClassType(), hitShip);
                defendingShips.remove(hitShip);
                hitShips.remove(hitShip.getShipClassType(), hitShip);
                break;
            }
        }
    }

    private void resolveScatterShooting(ListMultimap<ShipClassType, Ship> hitShips, ListMultimap<ShipClassType, Ship> hitDestroyedShips, ArrayList<Ship> defendingShips, ShipGroupTemplate shootingShipConfig) {
        Ship hitShip;
        for (Weapon shipShoot : shootingShipConfig.getWeaponSlots().values().stream().filter(Objects::nonNull).collect(Collectors.toList())) {
            hitShip = drawShipToShoot(defendingShips);
            hitShip.takeHit(shipShoot);
            if (!hitShip.getAlive()) {
                hitDestroyedShips.put(hitShip.getShipClassType(), hitShip);
                defendingShips.remove(hitShip);
                hitShips.remove(hitShip.getShipClassType(), hitShip);
                break;
            }
        }
    }

    private Ship drawShipToShoot(List<Ship> defendingShips) {
        int shipToShoot = randomGenerator.nextInt(defendingShips.size());
        return defendingShips.get(shipToShoot);
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
