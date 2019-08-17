package com.holkiew.yomenik.user.domian.port;

import com.holkiew.yomenik.user.domian.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);

}
