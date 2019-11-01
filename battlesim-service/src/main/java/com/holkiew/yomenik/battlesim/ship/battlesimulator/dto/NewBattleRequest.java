package com.holkiew.yomenik.battlesim.ship.battlesimulator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
public class NewBattleRequest {
    @NotNull
    private Map<String, Long> army1;
    @NotNull
    private Map<String, Long> army2;
    @NotNull
    private String army1UserId;
    @NotNull
    private String army2UserId;

    public NewBattleRequest(@NotNull Map<String, Long> army1, @NotNull String army1UserId, @NotNull Map<String, Long> army2, @NotNull String army2UserId) {
        this.army1 = army1;
        this.army2 = army2;
        this.army1UserId = army1UserId;
        this.army2UserId = army2UserId;
    }
}
