package com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request;

import com.holkiew.yomenik.battlesim.common.model.ShipClassType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.hull.HullType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.FireMode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class NewShipGroupTemplateRequest {
    @NotBlank
    private String templateName;
    @NotNull
    private ShipClassType shipClassType;
    @NotNull
    private HullType hullType;
    @NotNull
    private Map<Integer, Weapon> weaponSlots;
    @NotNull
    private FireMode fireMode;
}
