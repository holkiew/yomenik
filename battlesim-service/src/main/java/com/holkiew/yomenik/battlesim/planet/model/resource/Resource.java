package com.holkiew.yomenik.battlesim.planet.model.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
public abstract class Resource {
    private long amount;
    @JsonIgnore
    private LocalDateTime lastIncomeAddition;

    public Resource(long amount) {
        this.amount = amount;
        this.lastIncomeAddition = LocalDateTime.now();
    }

    public Resource(long amount, LocalDateTime lastIncomeAddition) {
        this.amount = amount;
        this.lastIncomeAddition = lastIncomeAddition;
    }

    public void updateAmountByIncome(long incomePerHour) {
        LocalDateTime now = LocalDateTime.now();
        long between = ChronoUnit.MILLIS.between(lastIncomeAddition, now);
        float income = ((float) between / 3600_000) * incomePerHour;
        this.lastIncomeAddition = now;
        this.amount += income;
    }

    public void add(Resource resource) {
        this.amount += resource.getAmount();
    }

    public void subtract(Resource resource) {
        this.amount -= resource.getAmount();
    }
}
