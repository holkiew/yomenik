package com.holkiew.yomenik.battlesim.ship.battlesimulator.model;

import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.Ship;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ArmyRecap {
    private Map<ShipType, Long> ships;
    private Map<ShipType, Long> destroyedShips;
    private String userId;

    public ArmyRecap(ListMultimap<ShipType, Ship> ships, ListMultimap<ShipType, Ship> destroyedShips, String userId) {
        this.ships = collectFromMultimap(ships);
        this.userId = userId;
        this.destroyedShips = collectFromMultimap(destroyedShips);
    }

    private Map<ShipType, Long> collectFromMultimap(ListMultimap<ShipType, Ship> ships) {
        return ships.asMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> (long) (entry.getValue().size())));
    }
}
