package com.holkiew.yomenik.battlesim.ship.battlesimulator.model;

import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.Ship;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipClassType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ArmyRecap {
    private Map<ShipClassType, Long> ships;
    private Map<ShipClassType, Long> destroyedShips;
    private String userId;

    public ArmyRecap(ListMultimap<ShipClassType, Ship> ships, ListMultimap<ShipClassType, Ship> destroyedShips, String userId) {
        this.ships = collectFromMultimap(ships);
        this.userId = userId;
        this.destroyedShips = collectFromMultimap(destroyedShips);
    }

    private Map<ShipClassType, Long> collectFromMultimap(ListMultimap<ShipClassType, Ship> ships) {
        return ships.asMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> (long) (entry.getValue().size())));
    }
}
