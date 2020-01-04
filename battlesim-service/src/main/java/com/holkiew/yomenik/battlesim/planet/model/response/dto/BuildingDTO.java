package com.holkiew.yomenik.battlesim.planet.model.response.dto;

import com.holkiew.yomenik.battlesim.planet.entity.Building;
import lombok.Data;

import java.util.List;

@Data
public class BuildingDTO {
    private int level;
    private int slot;
    private String type;
    private String label;
    private List<String> included;
    private List<String> excluded;

    BuildingDTO(Building building) {
        this.level = building.getLevel();
        this.slot = building.getSlot();
        this.type = building.getBuildingType().getName();
        this.label = building.getBuildingType().getLabel();
        this.included = building.getIncluded();
        this.excluded = building.getExcluded();
    }
}
