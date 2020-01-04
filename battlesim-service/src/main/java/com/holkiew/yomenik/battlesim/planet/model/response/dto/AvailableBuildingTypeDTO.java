package com.holkiew.yomenik.battlesim.planet.model.response.dto;

import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import lombok.Data;

@Data
public class AvailableBuildingTypeDTO {
    private String type;
    private String label;

    AvailableBuildingTypeDTO(BuildingType buildingType) {
        this.type = buildingType.getName();
        this.label = buildingType.getLabel();
    }
}
