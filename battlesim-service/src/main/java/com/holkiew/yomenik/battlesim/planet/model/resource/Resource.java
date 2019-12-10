package com.holkiew.yomenik.battlesim.planet.model.resource;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
public abstract class Resource {
    private long amount;

    private LocalDateTime lastIncomeAddition;

    @Transient
    private long incomeRatePerHour;

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
        this.incomeRatePerHour = incomePerHour;
    }

    public void add(Resource resource) {
        this.amount += resource.getAmount();
    }

    public void subtract(Resource resource) {
        this.amount -= resource.getAmount();
    }
}
