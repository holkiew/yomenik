package com.holkiew.yomenik.user.config.mapper;

import com.holkiew.yomenik.user.service.dto.NewUserRequest;
import com.holkiew.yomenik.user.service.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        newUserRequestMapping(modelMapper);
        return modelMapper;
    }

    private void newUserRequestMapping(ModelMapper modelMapper) {
        var typeMap = modelMapper.createTypeMap(NewUserRequest.class, User.class);
        typeMap.addMapping(NewUserRequest::getId, (destination, value) -> destination.setId(Objects.nonNull(value) ? value.toString() : User.createUniqueId()));
    }
}
