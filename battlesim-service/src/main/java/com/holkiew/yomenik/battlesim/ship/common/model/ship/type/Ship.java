package com.holkiew.yomenik.battlesim.ship.common.model.ship.type;


import lombok.Data;

@Data
public abstract class Ship {
    private Integer healthPoints;
    private Integer damage;
    private Boolean alive;

    public Ship(Integer healthPoints, Integer damage) {
        this.healthPoints = healthPoints;
        this.damage = damage;
        this.alive = healthPoints > 0;
    }

    public void takeHit(int damage) {
        this.healthPoints -= damage;
        this.alive = this.healthPoints > 0;
    }
}
