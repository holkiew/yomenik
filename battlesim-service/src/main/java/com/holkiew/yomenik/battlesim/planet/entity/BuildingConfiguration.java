package com.holkiew.yomenik.battlesim.planet.entity;


import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class BuildingConfiguration {
    @Id
    private String id;
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    private Map<BuildingType, Map<Integer, BuildingResources>> configuration;

    @Data
    @AllArgsConstructor
    public static class BuildingResources {
        public Resources cost;
        public Resources output;
    }
}
