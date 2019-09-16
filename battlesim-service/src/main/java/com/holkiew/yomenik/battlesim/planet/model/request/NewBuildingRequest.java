package com.holkiew.yomenik.battlesim.planet.model.request;

import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewBuildingRequest {

    @NotBlank
    private String planetId;
    @NotNull
    private Integer slot;
    @NotNull
    private BuildingType buildingType;
}
