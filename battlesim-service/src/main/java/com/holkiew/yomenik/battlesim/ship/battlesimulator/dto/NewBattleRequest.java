package com.holkiew.yomenik.battlesim.ship.battlesimulator.dto;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class NewBattleRequest {
    @NotNull
    private Map<ShipType, Long> army1;
    @NotNull
    private Map<ShipType, Long> army2;
    @NotNull
    @Min(1)
    private Long stageDelay;
}
