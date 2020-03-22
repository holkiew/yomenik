package com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class DeleteShipGroupTemplateRequest {
    @NotBlank
    private String templateName;
}
