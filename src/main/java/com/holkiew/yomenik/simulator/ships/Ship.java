package com.holkiew.yomenik.simulator.ships;


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

    public Ship takeHit(int damage){
        this.healthPoints -= damage;
        this.alive = this.healthPoints > 0;
        return this;
    }
}
