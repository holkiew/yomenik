package com.holkiew.yomenik.battlesim.ship.travel.model;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@ToString
@Getter
public class Fleet {
    private Map<ShipType, Long> ships;
    private boolean onRoute;
    private String planetIdFrom;
    private String planetIdTo;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public Fleet(Map<ShipType, Long> ships) {
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
        if (onRoute) {
            onRoute = LocalDateTime.now().isAfter(arrivalTime);
        }
        return onRoute;
    }
}
