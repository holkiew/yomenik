package com.holkiew.yomenik.gateway.authentication.dto.userservice;

import com.holkiew.yomenik.gateway.authentication.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class NewUserRequest {

    private String id;
    private String username;
    private String password;
    private String enabled;
    private List<Role> roles;
}
