package com.holkiew.yomenik.battlesim.planet.entity;

import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    @Transient
    private List<String> included;
    @Transient
    private List<String> excluded;
}
