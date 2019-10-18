package com.holkiew.yomenik.battlesim.galaxy.entity;

import com.holkiew.yomenik.battlesim.galaxy.model.Coordinates;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolarSystem {

    @Id
    private String id;
    private int galaxyId;
    private Map<Coordinates, String> planetsCoordinatesIds;
    // TODO
//    private List<String> neighbouringSolarSystemIds;
    private String prevSolarSystemId;
    private String NextSolarSystemId;
}
