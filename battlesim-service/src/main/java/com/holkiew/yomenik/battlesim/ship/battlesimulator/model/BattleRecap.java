package com.holkiew.yomenik.battlesim.ship.battlesimulator.model;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStage;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleRecap {
    private ArmyRecap army1Recap;
    private ArmyRecap army2Recap;
    private BattleStage battleStage;
    private LocalDateTime issueTime;
    private Boolean isIssued;

    public BattleRecap(BattleStrategy battleStrategy, LocalDateTime issueTime, String army1UserId, String army2UserId) {
        this.battleStage = battleStrategy.getBattleStage();
        this.army1Recap = new ArmyRecap(battleStrategy.getArmy1().getShips(), battleStrategy.getArmy1().getDestroyedShips(), army1UserId);
        this.army2Recap = new ArmyRecap(battleStrategy.getArmy2().getShips(), battleStrategy.getArmy2().getDestroyedShips(), army2UserId);
        this.issueTime = issueTime;
    }

    public Boolean getIsIssued() {
        return LocalDateTime.now().isAfter(issueTime);
    }

    public Boolean getIsNotIssued() {
        return !getIsIssued();
    }
}
