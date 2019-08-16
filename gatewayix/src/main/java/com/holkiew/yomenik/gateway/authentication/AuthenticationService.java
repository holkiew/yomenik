package com.holkiew.yomenik.gateway.authentication;

import com.holkiew.yomenik.gateway.authentication.dto.service.AuthRequest;
import com.holkiew.yomenik.gateway.authentication.dto.service.RegisterRequest;
import com.holkiew.yomenik.gateway.authentication.entity.Role;
import com.holkiew.yomenik.gateway.authentication.entity.User;
import com.holkiew.yomenik.gateway.authentication.port.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void injectDefaultUsers() {
        User admin = new User("admin", passwordEncoder.encode("admin"), true, Arrays.asList((Role.ROLE_ADMIN)));
        User user = new User("user", passwordEncoder.encode("user"), true, Arrays.asList((Role.ROLE_USER)));

        Flux.just(admin, user)
                .flatMap(usr -> Mono.just(Tuples.of(usr, userRepository.existsByUsername(usr.getUsername()))))
                .filterWhen(tuple -> BooleanUtils.not(tuple.getT2()))
                .flatMap(tuple -> userRepository.save(tuple.getT1()))
                .log()
                .doOnComplete(() -> log.info("Initial user injection complete"))
                .subscribe();
    }

    public Mono<User> login(AuthRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.encode(request.getPassword()).equals(user.getPassword()))
                .switchIfEmpty(Mono.error(new Exception("Wrong username or password")));
    }

    public Mono<User> registerNewUser(RegisterRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> Mono.error(new Exception("User already exists")))
                .defaultIfEmpty(Mono.just(User.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .roles(Collections.singletonList(Role.ROLE_USER))
                        .enabled(true)
                        .build()
                ))
                .cast(User.class)
                .flatMap(userRepository::save);
    }

}
