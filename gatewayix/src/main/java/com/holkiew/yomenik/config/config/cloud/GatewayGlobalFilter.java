package com.holkiew.yomenik.config.config.cloud;

import com.holkiew.yomenik.config.authentication.LocalPrincipal;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayGlobalFilter {

    @Bean
    public GlobalFilter addPrincipalInfoHeader() {
        return (exchange, chain) -> exchange.getPrincipal()
                .cast(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .defaultIfEmpty(Mono.error(new AuthenticationCredentialsNotFoundException("Filter error")))
                .cast(LocalPrincipal.class)
                .map(principal -> {
                    exchange.getRequest().mutate()
                            .header("PRINCIPAL-NAME", principal.getName())
                            .header("PRINCIPAL-ID", principal.getId())
                            .build();
                    return exchange;
                })
                .flatMap(chain::filter);
    }
}
