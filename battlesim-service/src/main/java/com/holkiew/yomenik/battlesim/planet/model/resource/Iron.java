package com.holkiew.yomenik.battlesim.planet.model.resource;

import java.time.LocalDateTime;

public class Iron extends Resource {
    public Iron(long amount) {
        super(amount);
    }

    public Iron(long amount, LocalDateTime lastIncomeAddition) {
        super(amount, lastIncomeAddition);
    }

}
