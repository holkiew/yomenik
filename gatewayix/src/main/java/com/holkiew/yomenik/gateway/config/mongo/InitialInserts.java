package com.holkiew.yomenik.gateway.config.mongo;

import com.holkiew.yomenik.gateway.authentication.entity.User;
import com.holkiew.yomenik.gateway.authentication.model.Role;
import com.holkiew.yomenik.gateway.authentication.port.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.retry.Retry;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Log4j2
//TODO do wywalenia na rzecz insertu z pliku najlepiej
public class InitialInserts {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostConstruct
    public void injectDefaultUsers() {
        User admin = new User("admin", passwordEncoder.encode("admin"), true, Arrays.asList((Role.ROLE_ADMIN)));
        User user = new User("user", passwordEncoder.encode("user"), true, Arrays.asList((Role.ROLE_USER)));
        AtomicInteger retryCounter = new AtomicInteger(1);
        Flux.just(admin, user)
                .flatMap(usr -> Mono.just(Tuples.of(usr, userRepository.existsByUsername(usr.getUsername()))))
                .filterWhen(tuple -> BooleanUtils.not(tuple.getT2()))
                .flatMap(tuple -> userRepository.save(tuple.getT1()))
                .doOnNext(usr -> log.info("Saved user:" + user))
                .doOnComplete(() -> log.info("Initial user injection complete"))
                .delaySubscription(Duration.ofSeconds(3))
                .retryWhen(Retry.any()
                        .fixedBackoff(Duration.ofSeconds(10))
                        .doOnRetry(context -> log.warn("No connection to user-service, retry " + retryCounter.addAndGet(1))))
                .subscribe();

    }
}
