package com.holkiew.yomenik.gateway.authentication.port;

import com.holkiew.yomenik.gateway.authentication.entity.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository {

    Mono<User> findByUsername(String username);
    Mono<Boolean> existsByUsername(String username);

    Mono<User> save(User user);

}
