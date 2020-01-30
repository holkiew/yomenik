package com.holkiew.yomenik.battlesim.planet.model.resource;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class Concrete extends Resource {
    public Concrete(long amount) {
        super(amount);
    }

    public Concrete(long amount, LocalDateTime lastIncomeAddition) {
        super(amount, lastIncomeAddition);
    }
}
