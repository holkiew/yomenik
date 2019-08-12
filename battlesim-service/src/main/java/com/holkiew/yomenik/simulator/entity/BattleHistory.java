package com.holkiew.yomenik.simulator.entity;

import com.holkiew.yomenik.simulator.ship.battle.BattleStage;
import com.holkiew.yomenik.simulator.ship.battle.BattleStrategy;
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
    private Map<BattleStage, BattleRecap> battleRecapMap;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isIssued;
    private Long stageDelay;

    public BattleHistory(BattleStrategy battleStrategy, LocalDateTime startTime, long stageDelay) {
        this.id = UUID.randomUUID().toString();
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
