package com.holkiew.yomenik.battlesim.planet.engine;

import com.holkiew.yomenik.battlesim.planet.entity.Building;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingRulesEngine {

    public void fillBuildingRules(Building building) {
        building.setExcluded(List.of("ALL"));
        building.setIncluded(List.of(building.getBuildingType().getName()));
    }
}
