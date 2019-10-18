package com.holkiew.yomenik.battlesim.ship.battlesimulator.entity;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStage;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStrategy;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.model.BattleRecap;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Document
@Data
@NoArgsConstructor
public class BattleHistory {

    @Id
    private String id;
    private String userId;
    private Map<BattleStage, BattleRecap> battleRecapMap;
    private LocalDateTime startDate;
    private LocalDateTime nextRoundDate;
    private LocalDateTime endDate;
    private Boolean isIssued;
    private Long stageDelay;
    private BattleStage currentStage;

    public BattleHistory(BattleStrategy battleStrategy, String userId, LocalDateTime startTime, long stageDelay) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.startDate = startTime;
        this.battleRecapMap = new EnumMap<>(BattleStage.class);
        this.isIssued = false;
        this.addNewEntry(battleStrategy, startTime);
        this.nextRoundDate = startTime;
        this.stageDelay = stageDelay;
        this.currentStage = BattleStage.NEW;
    }

    public void addNewEntry(BattleStrategy battleStrategy, LocalDateTime stageTime) {
        this.battleRecapMap.put(battleStrategy.getBattleStage(), new BattleRecap(battleStrategy, stageTime));
    }
}
