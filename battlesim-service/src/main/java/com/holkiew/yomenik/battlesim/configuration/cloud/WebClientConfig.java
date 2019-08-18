package com.holkiew.yomenik.battlesim.configuration.cloud;

import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import springfox.documentation.spring.web.WebFluxObjectMapperConfigurer;

@Configuration
public class WebClientConfig extends WebFluxObjectMapperConfigurer {

    @Bean
    WebClient webClient(LoadBalancerExchangeFilterFunction lbFilter) {
        return WebClient.builder()
                .filter(lbFilter)
                .build();
    }
}
