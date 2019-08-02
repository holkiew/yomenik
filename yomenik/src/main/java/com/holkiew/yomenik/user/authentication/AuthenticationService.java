package com.holkiew.yomenik.user.authentication;

import com.holkiew.yomenik.user.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
public class AuthenticationService {

    //username:passwowrd -> user:user
    private final String userUsername = "user";// password: user
    private final User user = new User(userUsername, "user", true, Arrays.asList((Role.ROLE_USER)));

    //username:passwowrd -> admin:admin
    private final String adminUsername = "admin";// password: admin
    private final User admin = new User(adminUsername, "admin", true, Arrays.asList((Role.ROLE_ADMIN)));

    public Mono<User> findByUsername(String username) {
        if (username.equals(userUsername)) {
            return Mono.just(user);
        } else if (username.equals(adminUsername)) {
            return Mono.just(admin);
        } else {
            return Mono.empty();
        }
    }

}
