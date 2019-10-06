package com.holkiew.yomenik.battlesim.planet.entity;

import com.holkiew.yomenik.battlesim.common.YamlPropertiesLoader;
import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import com.holkiew.yomenik.battlesim.planet.model.resource.Iron;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Building {
    public static int BASE_INCOME;
    public static float BASE_INCOME_PER_LEVEL_INCREASE;
    public static Resources BASE_COST;
    public static float BASE_COST_PER_LEVEL_INCREASE;

    @Id
    private int id;
    private int level;
    private int slot;
    private BuildingType buildingType;

    protected static <T> void loadCommonStaticVariables(Class<T> tClass) {
        Map<Object, Object> objectMap = YamlPropertiesLoader.getObjectMap(tClass);
        BASE_INCOME = (int) objectMap.get("baseIncome");
        BASE_INCOME_PER_LEVEL_INCREASE = (float) (double) objectMap.get("increasePerLevel");
        BASE_COST = YamlPropertiesLoader.load((Map) objectMap.get("baseCost"), Resources.class);
        BASE_COST_PER_LEVEL_INCREASE = (float) (double) objectMap.get("baseCostIncreasePerLevel");
    }

    public Resources getLevelCost(long level) {
        Iron iron = new Iron((long) (Math.pow(BASE_COST_PER_LEVEL_INCREASE, level) * BASE_COST.getIron().getAmount()), null);
        return new Resources(iron);
    }
}
