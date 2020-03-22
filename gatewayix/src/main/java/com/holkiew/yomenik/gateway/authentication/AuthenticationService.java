package com.holkiew.yomenik.gateway.authentication;

import com.holkiew.yomenik.gateway.authentication.dto.service.AuthRequest;
import com.holkiew.yomenik.gateway.authentication.dto.service.RegisterRequest;
import com.holkiew.yomenik.gateway.authentication.entity.User;
import com.holkiew.yomenik.gateway.authentication.model.Role;
import com.holkiew.yomenik.gateway.authentication.port.UserRepositoryPort;
import com.holkiew.yomenik.gateway.authentication.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    public Mono<String> login(AuthRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.encode(request.getPassword()).equals(user.getPassword()))
                .map(jwtUtils::generateToken)
                .switchIfEmpty(Mono.error(new Exception("Wrong username or password")));
    }

    public Mono<String> registerNewUser(RegisterRequest request) {
        return userRepository.save(
                User.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .roles(Collections.singletonList(Role.ROLE_USER))
                        .enabled(true)
                        .build())
                .map(jwtUtils::generateToken)
                .switchIfEmpty(Mono.error(new Exception("User already exists")));


    }

}
