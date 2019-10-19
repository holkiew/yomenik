package com.holkiew.yomenik.battlesim.ship.battlesimulator.dto;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
public class NewBattleRequest {
    @NotNull
    private Map<ShipType, Long> army1;
    @NotNull
    private Map<ShipType, Long> army2;
    private String army1UserId;
    private String army2UserId;

    public NewBattleRequest(@NotNull Map<ShipType, Long> army1, String army1UserId, @NotNull Map<ShipType, Long> army2, String army2UserId) {
        this.army1 = army1;
        this.army2 = army2;
        this.army1UserId = army1UserId;
        this.army2UserId = army2UserId;
    }
}
