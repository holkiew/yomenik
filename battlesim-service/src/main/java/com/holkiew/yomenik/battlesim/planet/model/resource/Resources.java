package com.holkiew.yomenik.battlesim.planet.model.resource;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Resources {
    @Builder.Default
    private Iron iron = new Iron(0);
    @Builder.Default
    private Crystal crystal = new Crystal(0);
    @Builder.Default
    private Concrete concrete = new Concrete(0);

    public boolean hasMoreOrEqual(Resources otherResources) {
        long otherIron = otherResources.getIron().getAmount();
        return this.iron.getAmount() > otherIron;
    }

    public void add(Resources otherResources) {
        this.iron.add(otherResources.getIron());
        this.crystal.add(otherResources.getCrystal());
        this.concrete.add(otherResources.getConcrete());
    }

    public void subtract(Resources otherResources) {
        this.iron.subtract(otherResources.getIron());
        this.crystal.subtract(otherResources.getCrystal());
        this.concrete.subtract(otherResources.getConcrete());
    }

    public void divide(double divisor) {
        this.iron.setAmount((long) (this.iron.getAmount() / divisor));
        this.crystal.setAmount((long) (this.crystal.getAmount() / divisor));
        this.concrete.setAmount((long) (this.concrete.getAmount() / divisor));
    }
}
