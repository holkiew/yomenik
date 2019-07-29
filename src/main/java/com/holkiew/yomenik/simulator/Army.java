package com.holkiew.yomenik.simulator;

import com.holkiew.yomenik.simulator.ships.Ship;
import com.holkiew.yomenik.simulator.ships.ShipFactory;
import com.holkiew.yomenik.simulator.ships.ShipName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Army {
    private List<Ship> shipsLevel1;
    private List<Ship> destroyedShipsLevel1 = new ArrayList<>();

    public Army(List<Ship> shipsLevel1) {
        this.shipsLevel1 = shipsLevel1;
    }

    public static Army of(Map<ShipName, Long> map) {
        var ships = getShipsFromMap(map);
        return new Army(ships);
    }

    private static List<Ship> getShipsFromMap(Map<ShipName, Long> map) {
        return map.entrySet().stream()
                .filter(entry->Objects.nonNull(entry.getValue()))
                .map(entry -> Stream.generate(
                        () -> ShipFactory.getShip(entry.getKey()))
                        .parallel().limit(entry.getValue())
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("Army{" +
                "\n\t(%d)shipsLevel1=" + shipsLevel1 +
                "\n\t(%d)destroyedShipsLevel1=" + destroyedShipsLevel1 +
                '}', shipsLevel1.size(), destroyedShipsLevel1.size());
    }
}
