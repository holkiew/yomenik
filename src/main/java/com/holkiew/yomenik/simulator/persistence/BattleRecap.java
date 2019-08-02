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

    public BattleRecap(BattleStrategy battleStrategy, LocalDateTime issueTime){
        this.battleStage = battleStrategy.getBattleStage();
        this.army1Recap = new ArmyRecap(battleStrategy.getArmy1().getShips().size(), battleStrategy.getArmy1().getDestroyedShips().size());
        this.army2Recap = new ArmyRecap(battleStrategy.getArmy2().getShips().size(), battleStrategy.getArmy2().getDestroyedShips().size());
        this.issueTime = issueTime;
        this.isIssued = false;
    }

    public Boolean getIsNotIssued(){
        return !this.isIssued;
    }
}
