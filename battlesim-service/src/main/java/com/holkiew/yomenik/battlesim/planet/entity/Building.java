package com.holkiew.yomenik.battlesim.planet.entity;

import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import com.holkiew.yomenik.battlesim.planet.model.resource.Iron;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Building {
    @Id
    private int id;
    private int level;
    private int slot;
    private BuildingType buildingType;

    public Resources getLevelCost(long level) {
        IronMineProperties properties = (IronMineProperties) BuildingType.IRON_MINE.getProperties();
        Iron iron = new Iron((long) (Math.pow(properties.baseCostIncreasePerLevel, level) * properties.baseCost.getIron().getAmount()), null);
        return new Resources(iron);
    }
}
