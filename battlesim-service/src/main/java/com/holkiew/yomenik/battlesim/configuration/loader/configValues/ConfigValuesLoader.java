package com.holkiew.yomenik.battlesim.configuration.loader.configValues;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@PropertySource("classpath:config_values/Buildings.yml")
public class ConfigValuesLoader {

    @Bean("mineProps")
    @ConfigurationProperties("mine")
    public Map<String, String> mineProps(Map<String, String> mine) {
        return mine;
    }
}
