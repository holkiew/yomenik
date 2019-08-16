package com.holkiew.yomenik.gateway.authentication.adapter;

import com.holkiew.yomenik.gateway.authentication.dto.userservice.NewUserRequest;
import com.holkiew.yomenik.gateway.authentication.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter {
    private final WebClient webclient;
    private final ModelMapper modelMapper;

    //    @Override
    public Mono<User> findByUsername(String username) {
        return webclient.get().uri("user-service/user", username)
                .retrieve().bodyToMono(User.class);
    }

    //    @Override
    public Mono<Boolean> existsByUsername(String username) {
        return webclient.get().uri("user-service/user", username)
                .retrieve().bodyToMono(User.class)
                .map(user -> true);
    }

    public Mono<User> save(User user) {
        NewUserRequest body = modelMapper.map(user, NewUserRequest.class);
        return webclient.post().uri("user-service/user")
                .body(BodyInserters.fromObject(body))
                .retrieve().bodyToMono(User.class);
    }
}
