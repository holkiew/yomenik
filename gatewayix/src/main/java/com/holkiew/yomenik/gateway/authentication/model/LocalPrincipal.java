package com.holkiew.yomenik.gateway.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;

@Data
@AllArgsConstructor
public class LocalPrincipal implements Principal {

    private String username;
    private String id;

    @Override
    public String getName() {
        return this.username;
    }
}
