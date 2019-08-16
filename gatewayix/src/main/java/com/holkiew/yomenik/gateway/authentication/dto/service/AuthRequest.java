package com.holkiew.yomenik.gateway.authentication.dto.service;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}