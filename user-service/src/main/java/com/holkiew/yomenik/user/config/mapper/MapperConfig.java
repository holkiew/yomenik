package com.holkiew.yomenik.user.config.mapper;

import com.holkiew.yomenik.user.domian.dto.NewUserRequest;
import com.holkiew.yomenik.user.domian.entity.User;
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
        //TODO te mappingi nie dzialaja i co wiecej psuuja domyslne dzialanie, zawsze rzucajac nulla
        var typeMap = modelMapper.createTypeMap(NewUserRequest.class, User.class);
        typeMap.addMapping(NewUserRequest::getId, (destination, value) -> destination.setId(Objects.nonNull(value) ? value.toString() : User.createUniqueId()));
        typeMap.addMappings(mapper -> mapper.when(Objects::isNull).map(NewUserRequest::getId, (destination, value) -> destination.setId(User.createUniqueId())));
    }
}
