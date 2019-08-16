package com.holkiew.yomenik.gateway.authentication.adapter;

import com.holkiew.yomenik.gateway.authentication.entity.LocalPrincipal;
import com.holkiew.yomenik.gateway.authentication.entity.Role;
import com.holkiew.yomenik.gateway.authentication.util.JWTUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthenticationManagerAdapter implements ReactiveAuthenticationManager {

    private final JWTUtils jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        String username;
        try {
            username = jwtUtil.getUsernameFromToken(authToken);
        } catch (Exception e) {
            username = null;
        }
        if (Objects.nonNull(username) && jwtUtil.validateToken(authToken)) {
            Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
            List<String> rolesMap = claims.get("role", List.class);
            List<Role> roles = rolesMap.stream().map(Role::valueOf).collect(Collectors.toList());

            var auth = new UsernamePasswordAuthenticationToken(
                    new LocalPrincipal(username, ""),
                    authToken,
                    roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList())
            );
            return Mono.just(auth);
        } else {
            return Mono.empty();
        }
    }
}
