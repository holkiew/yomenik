package com.holkiew.yomenik.battlesim.planet.model.resource;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class Crystal extends Resource {
    public Crystal(long amount) {
        super(amount);
    }

    public Crystal(long amount, LocalDateTime lastIncomeAddition) {
        super(amount, lastIncomeAddition);
    }
}
