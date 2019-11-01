package com.holkiew.yomenik.battlesim.ship.battlesimulator.model;

import com.google.common.collect.ListMultimap;
import com.holkiew.yomenik.battlesim.common.model.ShipClassType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.Ship;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ArmyRecap {
    private Map<String, Long> ships;
    private Map<String, Long> destroyedShips;
    private String userId;

    public ArmyRecap(ListMultimap<ShipClassType, Ship> ships, ListMultimap<ShipClassType, Ship> destroyedShips, String userId) {
        this.ships = collectFromMultimap(ships);
        this.userId = userId;
        this.destroyedShips = collectFromMultimap(destroyedShips);
    }

    private Map<String, Long> collectFromMultimap(ListMultimap<ShipClassType, Ship> ships) {
        return ships.asMap().entrySet()
                .stream().collect(Collectors.toMap(entry -> ((List<Ship>) entry.getValue()).get(0).getFleetGroupTemplateName(), entry -> (long) (entry.getValue().size())));
    }
}
