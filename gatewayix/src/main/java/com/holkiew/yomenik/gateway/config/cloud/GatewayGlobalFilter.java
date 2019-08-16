package com.holkiew.yomenik.gateway.config.cloud;

import com.holkiew.yomenik.gateway.authentication.entity.LocalPrincipal;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Configuration
public class GatewayGlobalFilter {

    @Bean
    public GlobalFilter addPrincipalInfoHeader() {
        return (exchange, chain) -> exchange.getPrincipal()
                .cast(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .cast(LocalPrincipal.class)
                .map(principal -> {
                    exchange.getRequest().mutate()
                            .header("PRINCIPAL-NAME", principal.getName())
                            .header("PRINCIPAL-ID", principal.getId())
                            .build();
                    return exchange;
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }
}
