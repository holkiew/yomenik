package com.holkiew.yomenik.battlesim.planet.entity;

import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Builder
@Document
public class Building {
    @Id
    private int id;
    private int level;
    private int slot;
    private BuildingType buildingType;
}
