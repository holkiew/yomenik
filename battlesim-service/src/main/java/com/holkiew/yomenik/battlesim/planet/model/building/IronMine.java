package com.holkiew.yomenik.battlesim.planet.model.building;

import com.holkiew.yomenik.battlesim.planet.entity.Building;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IronMine extends Building {
    public final static int BASE_INCOME = 3600;
    public final static float PER_LEVEL_INCREASE = 1.30f;
}
