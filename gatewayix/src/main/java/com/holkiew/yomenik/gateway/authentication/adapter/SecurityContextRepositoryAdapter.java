package com.holkiew.yomenik.gateway.authentication.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SecurityContextRepositoryAdapter implements ServerSecurityContextRepository {

    private final AuthenticationManagerAdapter authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet. SecurityContextRepository::save");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        ServerHttpRequest request = swe.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring(7);
            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
        } else {
            return Mono.empty();
        }
    }

}
