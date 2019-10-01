package com.holkiew.yomenik.battlesim.planet.model.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Resources {
    private Iron iron;

    public boolean hasMoreOrEqual(Resources otherResources) {
        long otherIron = otherResources.getIron().getAmount();
        return this.iron.getAmount() > otherIron;
    }

    public void add(Resources otherResources) {
        this.iron.add(otherResources.getIron());
    }

    public void subtract(Resources otherResources) {
        this.iron.subtract(otherResources.getIron());
    }

    public void divide(double divisor) {
        this.iron.setAmount((long) (this.iron.getAmount() / divisor));
    }
}
