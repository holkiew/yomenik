package com.holkiew.yomenik.simulator.persistence;

import com.holkiew.yomenik.simulator.BattleStrategy;
import com.holkiew.yomenik.simulator.BattleStage;
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

    public BattleRecap(BattleStrategy battleStrategy, LocalDateTime issueTime){
        this.battleStage = battleStrategy.getBattleStage();
        this.army1Recap = new ArmyRecap(battleStrategy.getArmy1().getShipsLevel1().size(), battleStrategy.getArmy1().getDestroyedShipsLevel1().size());
        this.army2Recap = new ArmyRecap(battleStrategy.getArmy2().getShipsLevel1().size(), battleStrategy.getArmy2().getDestroyedShipsLevel1().size());
        this.issueTime = issueTime;
        this.isIssued = false;
    }

    public Boolean getIsNotIssued(){
        return !this.isIssued;
    }
}
