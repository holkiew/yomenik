package com.holkiew.yomenik.gateway.authentication.dto.service;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @Size(min = 3, max = 12)
    private String username;
    @Size(min = 3, max = 12)
    private String password;
}
