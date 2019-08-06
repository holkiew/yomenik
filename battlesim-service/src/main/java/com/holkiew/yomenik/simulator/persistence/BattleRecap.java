package com.holkiew.yomenik.simulator.persistence;

import com.holkiew.yomenik.simulator.BattleStage;
import com.holkiew.yomenik.simulator.BattleStrategy;
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

    public BattleRecap(BattleStrategy battleStrategy, LocalDateTime issueTime) {
        this.battleStage = battleStrategy.getBattleStage();
        this.army1Recap = new ArmyRecap(battleStrategy.getArmy1().getShips(), battleStrategy.getArmy1().getDestroyedShips());
        this.army2Recap = new ArmyRecap(battleStrategy.getArmy2().getShips(), battleStrategy.getArmy2().getDestroyedShips());
        this.issueTime = issueTime;
        this.isIssued = false;
    }

    public Boolean getIsIssued() {
        return issueTime.isAfter(LocalDateTime.now());
    }

    public Boolean getIsNotIssued() {
        return !getIsIssued();
    }
}
