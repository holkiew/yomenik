package com.holkiew.yomenik.battlesim.ship.battlesimulator.battle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.battlesim.common.util.MultimapCollector;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.model.ArmyRecap;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.Ship;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipFactory;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import lombok.Data;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Data
public class Army {
    private ListMultimap<ShipType, Ship> ships;
    private ListMultimap<ShipType, Ship> destroyedShips;

    protected Army(ListMultimap<ShipType, Ship> ships) {
        this.ships = ships;
        this.destroyedShips = ArrayListMultimap.create();
    }

    protected Army(ListMultimap<ShipType, Ship> ships, ListMultimap<ShipType, Ship> destroyedShips) {
        this.ships = ships;
        this.destroyedShips = destroyedShips;
    }

    public static Army of(Map<ShipType, Long> map) {
        var ships = getShipsFromMap(map);
        return new Army(ships);
    }

    public static Army of(ArmyRecap armyRecap) {
        var ships = getShipsFromMap(armyRecap.getShips());
        var destroyedShips = getShipsFromMap(armyRecap.getDestroyedShips());
        return new Army(ships, destroyedShips);
    }

    private static ListMultimap<ShipType, Ship> getShipsFromMap(Map<ShipType, Long> map) {
        return (ListMultimap<ShipType, Ship>) map.entrySet().stream()
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
