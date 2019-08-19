package com.holkiew.yomenik.gateway.authentication.port;

import com.holkiew.yomenik.gateway.authentication.entity.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> findByUsername(String username);
    Mono<Boolean> existsByUsername(String username);
    Mono<User> save(User user);

}
