package com.holkiew.yomenik.user.service.dto;

import com.holkiew.yomenik.user.service.Role;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class NewUserRequest {
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String enabled;
    @NotEmpty
    private List<Role> roles;
}
