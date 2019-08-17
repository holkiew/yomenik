package com.holkiew.yomenik.gateway.authentication.adapter;

import com.holkiew.yomenik.gateway.authentication.model.LocalPrincipal;
import com.holkiew.yomenik.gateway.authentication.model.Role;
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
            List<String> rolesList = claims.get("role", List.class);
            String id = claims.get("id", String.class);
            List<Role> roles = rolesList.stream().map(Role::valueOf).collect(Collectors.toList());

            var auth = new UsernamePasswordAuthenticationToken(
                    new LocalPrincipal(username, id),
                    authToken,
                    roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList())
            );
            return Mono.just(auth);
        } else {
            return Mono.empty();
        }
    }
}
