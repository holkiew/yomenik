package com.holkiew.yomenik.gateway.config.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    // TODO nie dziala zbyt dobrze, wolny + customowe mappingi zawsze zwracaja nulle
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
