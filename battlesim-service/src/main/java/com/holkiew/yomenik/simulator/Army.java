package com.holkiew.yomenik.simulator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.simulator.persistence.ArmyRecap;
import com.holkiew.yomenik.simulator.ships.Ship;
import com.holkiew.yomenik.simulator.ships.ShipFactory;
import com.holkiew.yomenik.simulator.ships.ShipName;
import com.holkiew.yomenik.util.MultimapCollector;
import lombok.Data;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Data
public class Army {
    private ListMultimap<ShipName, Ship> ships;
    private ListMultimap<ShipName, Ship> destroyedShips;

    protected Army(ListMultimap<ShipName, Ship> ships) {
        this.ships = ships;
        this.destroyedShips = ArrayListMultimap.create();
    }

    protected Army(ListMultimap<ShipName, Ship> ships, ListMultimap<ShipName, Ship> destroyedShips) {
        this.ships = ships;
        this.destroyedShips = destroyedShips;
    }

    public static Army of(Map<ShipName, Long> map) {
        var ships = getShipsFromMap(map);
        return new Army(ships);
    }

    public static Army of(ArmyRecap armyRecap) {
        var ships = getShipsFromMap(armyRecap.getShips());
        var destroyedShips = getShipsFromMap(armyRecap.getDestroyedShips());
        return new Army(ships, destroyedShips);
    }

    private static ListMultimap<ShipName, Ship> getShipsFromMap(Map<ShipName, Long> map) {
        return (ListMultimap<ShipName, Ship>) map.entrySet().stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .map(entry -> Stream.generate(
                        () -> ShipFactory.getShip(entry.getKey()))
                        .parallel()
                        .limit(entry.getValue())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
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
