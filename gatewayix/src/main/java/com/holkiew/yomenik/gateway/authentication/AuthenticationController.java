package com.holkiew.yomenik.gateway.authentication;

import com.holkiew.yomenik.gateway.authentication.dto.service.AuthRequest;
import com.holkiew.yomenik.gateway.authentication.dto.service.AuthResponse;
import com.holkiew.yomenik.gateway.authentication.dto.service.RegisterRequest;
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

    private final AuthenticationService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest request) {
        return authService.login(request)
                .map(token -> ResponseEntity.ok(new AuthResponse(token)))
                .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthResponse>> registerNewUser(@RequestBody @Valid RegisterRequest request) {
        return authService.registerNewUser(request)
                .map(token -> ResponseEntity.ok(new AuthResponse(token)))
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

}