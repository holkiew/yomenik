package com.holkiew.yomenik.simulator.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArmyRecap {
    private Integer shipsLevel1;
    private Integer destroyedShipsLevel1;
}
