package com.holkiew.yomenik.battlesim.planet.model.research.properites;

import com.holkiew.yomenik.battlesim.planet.model.Properties;
import com.holkiew.yomenik.battlesim.planet.model.resource.Iron;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Data
public abstract class ResearchProperties implements Properties {
    protected static final String CLASSPATH = Properties.BASE_CLASSPATH + "research/";

    @Value(("${increasePerLevel}"))
    public float increasePerLevel;
    @Value(("${baseCostIncreasePerLevel}"))
    public float baseCostIncreasePerLevel;
    public Resources baseCost;

    @Autowired
    public void setBaseCost(@Value(("${baseCost.iron}")) long iron) {
        this.baseCost = new Resources(new Iron(iron, null));
    }

    public Resources getCostByLevel(int level) {
        Iron iron = new Iron((long) (Math.pow(baseCostIncreasePerLevel, level) * baseCost.getIron().getAmount()), null);
        return new Resources(iron);
    }

}
