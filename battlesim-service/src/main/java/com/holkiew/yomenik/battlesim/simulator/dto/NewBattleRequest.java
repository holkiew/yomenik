package com.holkiew.yomenik.battlesim.simulator.dto;

import com.holkiew.yomenik.battlesim.simulator.ship.type.ShipName;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class NewBattleRequest {
    @NotNull
    private Map<ShipName, Long> army1;
    @NotNull
    private Map<ShipName, Long> army2;
    @NotNull
    @Min(1)
    private Long stageDelay;
}