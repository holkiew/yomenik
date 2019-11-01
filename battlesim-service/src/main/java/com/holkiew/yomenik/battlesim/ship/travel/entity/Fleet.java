package com.holkiew.yomenik.battlesim.ship.travel.entity;

import com.google.common.collect.Lists;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissonType;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class Fleet {
    @Id
    private String id;
    private Map<String, Long> ships;
    private String planetIdFrom;
    private String planetIdTo;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private TravelMissonType missionType;
    private Boolean missionCompleted;
    private List<String> relatedMissions;
    private String relatedBattleHistoryId;

    public Fleet(Map<String, Long> ships) {
        this.id = UUID.randomUUID().toString();
        this.ships = ships;
    }

    public Fleet(Map<String, Long> ships, String relatedBattleHistoryId) {
        this.id = UUID.randomUUID().toString();
        this.ships = ships;
        this.relatedBattleHistoryId = relatedBattleHistoryId;
    }

    public void setRoute(String planetIdTo, String planetIdFrom, LocalDateTime arrivalTime, TravelMissonType travelMissonType) {
        this.planetIdFrom = planetIdFrom;
        this.planetIdTo = planetIdTo;
        this.departureTime = LocalDateTime.now();
        this.arrivalTime = arrivalTime;
        this.missionType = travelMissonType;
        this.missionCompleted = false;
    }

    public void setRouteOnPlanets(Planet planetTo, Planet planetFrom, LocalDateTime arrivalTime, TravelMissonType travelMissonType, String... relatedMissionsIds) {
        this.setRouteOnPlanets(planetTo, planetFrom, arrivalTime, travelMissonType);
        this.relatedMissions = Lists.newArrayList(relatedMissionsIds);
    }

    public void setRouteOnPlanets(Planet planetTo, Planet planetFrom, LocalDateTime arrivalTime, TravelMissonType travelMissonType, List<String> relatedMissionsIds) {
        this.setRouteOnPlanets(planetTo, planetFrom, arrivalTime, travelMissonType);
        this.relatedMissions = relatedMissionsIds;
    }

    public void setRouteOnPlanets(Planet planetTo, Planet planetFrom, LocalDateTime arrivalTime, TravelMissonType travelMissonType) {
        this.planetIdTo = planetTo.getId();
        this.planetIdFrom = planetFrom.getId();
        this.departureTime = LocalDateTime.now();
        this.arrivalTime = arrivalTime;
        this.missionType = travelMissonType;
        this.missionCompleted = false;

        planetTo.getOnRouteFleets().put(travelMissonType, id);
        planetFrom.getOnRouteFleets().put(travelMissonType, id);
    }

    @Transient
    public boolean isOnRoute() {
        return LocalDateTime.now().isBefore(arrivalTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fleet fleet = (Fleet) o;
        return id.equals(fleet.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
