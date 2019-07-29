package com.holkiew.yomenik.simulator.persistence;

import com.holkiew.yomenik.simulator.BattleStrategy;
import com.holkiew.yomenik.simulator.BattleStage;
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
    private Map<BattleStage, BattleRecap> battleRecapMap;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isIssued;

    public BattleHistory(BattleStrategy battleStrategy, LocalDateTime startTime){
        this.id = UUID.randomUUID().toString();
        this.startDate = startTime;
        this.battleRecapMap = new EnumMap<>(BattleStage.class);
        this.isIssued = false;
        this.addNewEntry(battleStrategy, startTime);
    }
    public void addNewEntry(BattleStrategy battleStrategy, LocalDateTime stageTime){
        this.battleRecapMap.put(battleStrategy.getBattleStage(), new BattleRecap(battleStrategy, stageTime));
    }
}
