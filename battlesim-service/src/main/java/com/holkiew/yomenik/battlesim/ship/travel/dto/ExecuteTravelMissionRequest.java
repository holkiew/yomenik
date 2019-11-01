package com.holkiew.yomenik.battlesim.ship.travel.dto;

import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissonType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class ExecuteTravelMissionRequest {
    @NotNull
    private String planetIdFrom;
    @NotNull
    private String planetIdTo;
    @NotNull
    private Map<String, Long> fleet;
    @NotNull
    private TravelMissonType missonType;
}
