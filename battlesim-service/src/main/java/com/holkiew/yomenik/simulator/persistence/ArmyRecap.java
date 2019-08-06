package com.holkiew.yomenik.simulator.persistence;

import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.simulator.ships.Ship;
import com.holkiew.yomenik.simulator.ships.ShipName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ArmyRecap {
    private Map<ShipName, Long> ships;
    private Map<ShipName, Long> destroyedShips;

    public ArmyRecap(ListMultimap<ShipName, Ship> ships, ListMultimap<ShipName, Ship> destroyedShips) {
        this.ships = collectFromMultimap(ships);
        this.destroyedShips = collectFromMultimap(destroyedShips);
    }

    private Map<ShipName, Long> collectFromMultimap(ListMultimap<ShipName, Ship> ships) {
        return ships.asMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> (long) (entry.getValue().size())));
    }
}
