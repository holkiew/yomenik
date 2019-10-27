package com.holkiew.yomenik.gateway.authentication.adapter;

import com.holkiew.yomenik.gateway.authentication.dto.userservice.NewUserRequest;
import com.holkiew.yomenik.gateway.authentication.entity.User;
import com.holkiew.yomenik.gateway.authentication.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final WebClient webclient;
    private final ModelMapper modelMapper;

    public Mono<User> findByUsername(String username) {
        return webclient.get().uri(uriBuilder ->
                uriBuilder.host("user-service")
                        .path("user")
                        .queryParam("username", username).build())
                .retrieve().bodyToMono(User.class);
    }

    public Mono<Boolean> existsByUsername(String username) {
        return findByUsername(username)
                .map(user -> true)
                .defaultIfEmpty(false);
    }

    public Mono<User> save(User user) {
        NewUserRequest body = modelMapper.map(user, NewUserRequest.class);
        return webclient.post().uri(uriBuilder ->
                uriBuilder.host("user-service")
                        .path("user").build())
                .body(BodyInserters.fromObject(body))
                .retrieve().bodyToMono(User.class);
    }
}
