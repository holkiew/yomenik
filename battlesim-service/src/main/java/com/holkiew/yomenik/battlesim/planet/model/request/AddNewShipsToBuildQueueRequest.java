package com.holkiew.yomenik.battlesim.planet.model.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
public class AddNewShipsToBuildQueueRequest {
    @NotBlank
    private String planetId;
    @NotNull
    private Map<String, Integer> shipTemplateAmount;
}
