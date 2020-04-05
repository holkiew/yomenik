package com.holkiew.yomenik.battlesim.planet.model.response;

import com.holkiew.yomenik.battlesim.common.model.ShipClassType;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AvailableShipsToBuildResponse {

    private Map<String, ShipClassType> availableTemplates;
}
