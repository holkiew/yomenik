package com.holkiew.yomenik.battlesim.planet.model.resource;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class Iron extends Resource {
    public Iron(long amount) {
        super(amount);
    }

    public Iron(long amount, LocalDateTime lastIncomeAddition) {
        super(amount, lastIncomeAddition);
    }
}
