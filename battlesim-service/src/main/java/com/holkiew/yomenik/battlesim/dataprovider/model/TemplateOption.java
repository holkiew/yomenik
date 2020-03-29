package com.holkiew.yomenik.battlesim.dataprovider.model;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateOption {
    private List<Weapon> weapons;
}

