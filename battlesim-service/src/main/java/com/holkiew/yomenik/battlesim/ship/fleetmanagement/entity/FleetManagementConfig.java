package com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity;

import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipClassType;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.ShipClassGroupTemplate;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FleetManagementConfig {
    @Id
    private String id;
    private Map<ShipClassType, ShipClassGroupTemplate> shipGroupTemplates;

    public String getUserId() {
        return this.id;
    }
}
