package com.holkiew.yomenik.gateway.config.cloud;

import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
        // TODO: nowa wersja clouda udostepnia reactiveLoadBalancer
        // TODO: takie commonowe bzdury do jednego projektu i jako dependency wrzucic
    WebClient webClient(LoadBalancerExchangeFilterFunction lbFilter) {
        return WebClient.builder()
                .filter(lbFilter)
                .build();
    }
}
