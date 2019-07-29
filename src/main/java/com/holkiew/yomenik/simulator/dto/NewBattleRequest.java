package com.holkiew.yomenik.simulator.dto;

import com.holkiew.yomenik.simulator.ships.ShipName;
import lombok.Data;

import java.util.Map;

@Data
public class NewBattleRequest {
    private Map<ShipName, Long> army1;
    private Map<ShipName, Long> army2;
}
