package com.holkiew.yomenik.battlesim.planet.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.holkiew.yomenik.battlesim.planet.model.resource.Iron;
import com.holkiew.yomenik.battlesim.planet.model.resource.Resources;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import com.holkiew.yomenik.battlesim.ship.travel.model.Fleet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Map;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "galaxy_coordinates", unique = true, def = "{'galaxyId' : 1, 'coordinateX': 1, 'coordinateY': 1}")
})
public class Planet {

    @Id
    private String id;
    private String userId;
    private int galaxyId;
    private int coordinateX;
    private int coordinateY;
    @Builder.Default
    private Resources resources = new Resources(new Iron(0));
    @Builder.Default
    private Map<Integer, Building> buildings = Maps.newHashMap();
    @Builder.Default
    private Map<ShipType, Long> residingFleet = Maps.newHashMap();
    @Builder.Default
    private List<Fleet> onRouteFleets = Lists.newArrayList();


    public Planet(String id, String userId, int galaxyId, Tuple2<Integer, Integer> coordinates) {
        this.id = id;
        this.userId = userId;
        this.galaxyId = galaxyId;
        this.coordinateX = coordinates.getT1();
        this.coordinateY = coordinates.getT2();
        this.resources = new Resources(new Iron(0));
        this.buildings = Maps.newHashMap();
        this.residingFleet = Maps.newHashMap();
        this.onRouteFleets = Lists.newArrayList();
    }

}
