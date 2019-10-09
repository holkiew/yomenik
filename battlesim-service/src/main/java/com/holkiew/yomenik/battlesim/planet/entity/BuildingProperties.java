package com.holkiew.yomenik.battlesim.planet.entity;

import com.holkiew.yomenik.battlesim.planet.model.resource.Iron;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Data
public abstract class BuildingProperties implements Properties {
    protected static final String CLASSPATH =  Properties.BASE_CLASSPATH + "buildings/";

    @Value(("${baseIncome}"))
    public int baseIncome;
    @Value(("${increasePerLevel}"))
    public float increasePerLevel;
    @Value(("${baseCostIncreasePerLevel}"))
    public float baseCostIncreasePerLevel;
    public Resources baseCost;

    @Autowired
    public void setBaseCost(@Value(("${baseCost.iron}"))long iron) {
        this.baseCost = new Resources(new Iron(iron,null));
    }

}
