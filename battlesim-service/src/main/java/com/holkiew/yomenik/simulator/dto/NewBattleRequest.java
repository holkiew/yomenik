package com.holkiew.yomenik.simulator.dto;

import com.holkiew.yomenik.simulator.ship.type.ShipName;
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
    @Min(1)
    private Long stageDelay;
}
