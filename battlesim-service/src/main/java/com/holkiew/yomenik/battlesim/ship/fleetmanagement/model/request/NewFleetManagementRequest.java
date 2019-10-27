package com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request;

import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.FireMode;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewFleetManagementRequest {
    @NotBlank
    private String userId;
    @NotNull
    private FireMode fireMode;
}
