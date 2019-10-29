package com.holkiew.yomenik.battlesim.ship.battlesimulator.battle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.battlesim.common.util.MultimapCollector;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.model.ArmyRecap;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.Ship;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipClassType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipFactory;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import lombok.Data;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Data
public class Army {
    private ListMultimap<ShipClassType, Ship> ships;
    private ListMultimap<ShipClassType, Ship> destroyedShips;
    private FleetManagementConfig fleetManagementConfig;

    protected Army(ListMultimap<ShipClassType, Ship> ships, ListMultimap<ShipClassType, Ship> destroyedShips, FleetManagementConfig fleetManagementConfig) {
        this.ships = ships;
        this.fleetManagementConfig = fleetManagementConfig;
        this.destroyedShips = destroyedShips;
    }

    public static Army of(Map<ShipClassType, Long> map, FleetManagementConfig fleetManagementConfig) {
        var ships = getShipsFromMap(map);
        return new Army(ships, ArrayListMultimap.create(), fleetManagementConfig);
    }

    public static Army of(ArmyRecap armyRecap, FleetManagementConfig fleetManagementConfig) {
        var ships = getShipsFromMap(armyRecap.getShips());
        var destroyedShips = getShipsFromMap(armyRecap.getDestroyedShips());
        return new Army(ships, destroyedShips, fleetManagementConfig);
    }

    private static ListMultimap<ShipClassType, Ship> getShipsFromMap(Map<ShipClassType, Long> map) {
        return (ListMultimap<ShipClassType, Ship>) map.entrySet().stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .map(entry -> Stream.generate(
                        () -> ShipFactory.getShip(entry.getKey()))
                        .parallel()
                        .limit(entry.getValue())
                        .filter(Optional::isPresent).map(Optional::get)
                        .collect(MultimapCollector.toMultimap(o -> entry.getKey()))
                ).reduce((map1, map2) -> {
                    map1.putAll(map2);
                    return map1;
                }).orElse(ArrayListMultimap.create());
    }

    @Override
    public String toString() {
        return String.format("Army{" +
                "\n\t(%d)ships=" + ships +
                "\n\t(%d)destroyedShips=" + destroyedShips +
                '}', ships.size(), destroyedShips.size());
    }
}
