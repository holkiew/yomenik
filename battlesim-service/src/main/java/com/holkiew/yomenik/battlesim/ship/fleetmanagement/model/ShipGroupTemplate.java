package com.holkiew.yomenik.battlesim.ship.fleetmanagement.model;

import com.holkiew.yomenik.battlesim.common.model.ShipClassType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.hull.Hull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ShipGroupTemplate {
    private String name;
    private ShipClassType shipClassType;
    private Hull hull;
    private FireMode fireMode;
}
