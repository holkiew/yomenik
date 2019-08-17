package com.holkiew.yomenik.gateway.config.cloud;

import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient(LoadBalancerExchangeFilterFunction lbFilter) {
        return WebClient.builder()
                .filter(lbFilter)
                .build();
    }
}
