package com.holkiew.yomenik.battlesim.ship.fleetmanagement.model;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipClassType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ShipClassGroupTemplate {
    private String name;
    private ShipClassType shipClassType;
    private Map<Integer, Weapon> weaponSlots;
    private FireMode fireMode;
}
