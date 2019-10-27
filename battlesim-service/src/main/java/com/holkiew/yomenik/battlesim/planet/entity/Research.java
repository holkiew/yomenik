package com.holkiew.yomenik.battlesim.planet.entity;

import com.holkiew.yomenik.battlesim.planet.model.research.ResearchType;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Research {
    @Id
    private String id;
    private Map<ResearchType, Integer> researchLevels;

    private String getUserId() {
        return this.id;
    }
}
