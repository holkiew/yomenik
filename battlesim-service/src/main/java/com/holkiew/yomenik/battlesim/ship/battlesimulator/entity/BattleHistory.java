package com.holkiew.yomenik.battlesim.ship.battlesimulator.entity;

import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStage;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStrategy;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.model.BattleRecap;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document
@Data
@NoArgsConstructor
public class BattleHistory {

    @Id
    private String id;
    private List<String> involvedUserIds;
    private Map<BattleStage, BattleRecap> battleRecapMap;
    private LocalDateTime startDate;
    private LocalDateTime nextRoundDate;
    private LocalDateTime endDate;
    private Boolean isIssued;
    private Long stageDelay;
    private BattleStage currentStage;

    public BattleHistory(BattleStrategy battleStrategy, LocalDateTime startTime, long stageDelay, String... involvedUserIds) {
        this.id = UUID.randomUUID().toString();
        this.involvedUserIds = Arrays.asList(involvedUserIds);
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
