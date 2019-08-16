package com.holkiew.yomenik.gateway.authentication.dto.service;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
