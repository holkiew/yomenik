package com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.FireMode;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ModifyFleetManagementRequest {
    @NotBlank
    private String userId;
    private FireMode fireMode;
}
