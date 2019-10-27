package com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.FireMode;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FleetManagementConfig {
    @Id
    private String id;
    private FireMode fireMode;

    public String getUserId() {
        return this.id;
    }
}
