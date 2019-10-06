package com.holkiew.yomenik.battlesim.ship.travel.entity;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class Fleet {
    @Id
    private String id;
    private Map<ShipType, Long> ships;
    private boolean onRoute;
    private String planetIdFrom;
    private String planetIdTo;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public Fleet(Map<ShipType, Long> ships) {
        this.id = UUID.randomUUID().toString();
        this.ships = ships;
    }

    public void setRoute(String planedIdTo, String planedIdFrom, LocalDateTime arrivalTime) {
        this.onRoute = true;
        this.planetIdFrom = planedIdFrom;
        this.planetIdTo = planedIdTo;
        this.departureTime = LocalDateTime.now();
        this.arrivalTime = arrivalTime;
    }

    public boolean isOnRoute() {
        return LocalDateTime.now().isBefore(arrivalTime);
    }
}
