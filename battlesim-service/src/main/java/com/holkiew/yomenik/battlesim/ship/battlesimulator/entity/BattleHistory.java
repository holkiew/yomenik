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
    private String army1UserId;
    private String army2UserId;
    private Map<BattleStage, BattleRecap> battleRecapMap;
    private LocalDateTime startDate;
    private LocalDateTime nextRoundDate;
    private LocalDateTime endDate;
    private Boolean isIssued;
    private Long stageDelay;
    private BattleStage currentStage;

    public BattleHistory(BattleStrategy battleStrategy, LocalDateTime startTime, long stageDelay, String army1UserId, String army2UserId) {
        this.id = UUID.randomUUID().toString();
        this.startDate = startTime;
        this.battleRecapMap = new EnumMap<>(BattleStage.class);
        this.isIssued = false;
        this.addNewEntry(battleStrategy, startTime);
        this.nextRoundDate = startTime;
        this.stageDelay = stageDelay;
        this.army1UserId = army1UserId;
        this.army2UserId = army2UserId;
        this.currentStage = BattleStage.NEW;
    }

    public void addNewEntry(BattleStrategy battleStrategy, LocalDateTime stageTime) {
        this.battleRecapMap.put(battleStrategy.getBattleStage(), new BattleRecap(battleStrategy, stageTime, army1UserId, army2UserId));
    }
}
