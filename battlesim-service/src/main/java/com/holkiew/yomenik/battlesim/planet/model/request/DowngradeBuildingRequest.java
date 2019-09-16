package com.holkiew.yomenik.battlesim.planet.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DowngradeBuildingRequest {
    @NotBlank
    private String planetId;
    @NotNull
    private Integer slot;
}
