package com.holkiew.yomenik.gateway.authentication;

import com.holkiew.yomenik.gateway.authentication.dto.service.AuthRequest;
import com.holkiew.yomenik.gateway.authentication.dto.service.AuthResponse;
import com.holkiew.yomenik.gateway.authentication.dto.service.RegisterRequest;
import com.holkiew.yomenik.gateway.authentication.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Log4j2
public class AuthenticationController {

    private final JWTUtils jwtUtils;

    private final AuthenticationService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest request) {
        return authService.login(request)
                .doOnError(throwable -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
                .map(user -> ResponseEntity.ok(new AuthResponse(jwtUtils.generateToken(user))));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<?>> registerNewUser(@RequestBody @Valid RegisterRequest request) {
        return authService.registerNewUser(request)
                .doOnError(throwable -> ResponseEntity.badRequest().build())
                .map(user -> ResponseEntity.ok().build());
    }

}