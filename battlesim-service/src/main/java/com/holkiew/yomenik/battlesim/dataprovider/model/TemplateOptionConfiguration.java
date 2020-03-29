package com.holkiew.yomenik.battlesim.dataprovider.model;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.hull.HullType;
import lombok.Data;

import java.util.Map;

@Data
public class TemplateOptionConfiguration {

    private Map<HullType, TemplateOption> hullOptions;
}
