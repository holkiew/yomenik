package com.holkiew.yomenik.battlesim.ship.common.model.ship.type;


import com.holkiew.yomenik.battlesim.common.model.ShipClassType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
public abstract class Ship {
    private Integer healthPoints;
    private Integer shieldsPoints;
    private Integer armorPoints;
    private ShipClassType shipClassType;
    private String fleetGroupTemplateName;
    @Transient
    private Boolean alive;

    public Ship(Integer healthPoints, Integer shieldsPoints, Integer armorPoints, ShipClassType shipClassType) {
        this.healthPoints = healthPoints;
        this.shieldsPoints = shieldsPoints;
        this.armorPoints = armorPoints;
        this.alive = healthPoints > 0;
        this.shipClassType = shipClassType;
    }

    public void takeHit(Weapon shipShot) {
        int shotDamage = shipShot.getBaseDamagePerHit();
        switch (shipShot.getWeaponType()) {
            case KINETIC:
                shieldsPoints = calculateLifeAfterHit(shieldsPoints, shotDamage);
                break;
            case LASER:
                armorPoints = calculateLifeAfterHit(armorPoints, shotDamage);
                break;
        }
        this.alive = this.healthPoints > 0;
    }

    public Ship withFleetGroupTemplateName(String fleetGroupTemplateName) {
        this.fleetGroupTemplateName = fleetGroupTemplateName;
        return this;
    }

    private Integer calculateLifeAfterHit(int life, int shotDamage) {
        if (life <= 0) {
            healthPoints -= shotDamage;
        }
        if (life >= shotDamage) {
            life -= shotDamage;
        } else {
            healthPoints -= shotDamage - shieldsPoints;
            life = 0;
        }
        return life;
    }
}
