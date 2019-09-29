package com.holkiew.yomenik.battlesim.ship.battlesimulator.battle;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

public enum BattleStage {
    NEW(0), ROUND_1(1), ROUND_2(2), ROUND_3(3), END(999);

    @Getter
    @JsonValue
    private int value;

    BattleStage(int stage) {
        this.value = stage;
    }

    public BattleStage nextStage() {
        return Stream.of(BattleStage.values())
                .filter(s -> s.value == this.value + 1)
                .findFirst()
                .orElse(END);
    }
}
