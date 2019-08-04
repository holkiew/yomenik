package com.holkiew.yomenik.config.authentication.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}