package com.holkiew.yomenik.battlesim.simulator.entity;

import com.holkiew.yomenik.battlesim.simulator.model.BattleRecap;
import com.holkiew.yomenik.battlesim.simulator.ship.battle.BattleStage;
import com.holkiew.yomenik.battlesim.simulator.ship.battle.BattleStrategy;
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
    private LocalDateTime endDate;
    private Boolean isIssued;
    private Long stageDelay;

    public BattleHistory(BattleStrategy battleStrategy, String userId, LocalDateTime startTime, long stageDelay) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.startDate = startTime;
        this.battleRecapMap = new EnumMap<>(BattleStage.class);
        this.isIssued = false;
        this.addNewEntry(battleStrategy, startTime);
        this.stageDelay = stageDelay;
    }

    public void addNewEntry(BattleStrategy battleStrategy, LocalDateTime stageTime) {
        this.battleRecapMap.put(battleStrategy.getBattleStage(), new BattleRecap(battleStrategy, stageTime));
    }
}
