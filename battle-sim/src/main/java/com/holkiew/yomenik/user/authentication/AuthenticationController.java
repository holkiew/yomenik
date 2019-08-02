package com.holkiew.yomenik.user.authentication;

import com.holkiew.yomenik.user.authentication.dto.AuthRequest;
import com.holkiew.yomenik.user.authentication.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final JWTUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest request) {
        return userRepository.findByUsername(request.getUsername()).map((userDetails) -> {
            if (passwordEncoder.encode(request.getPassword()).equals(userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(jwtUtils.generateToken(userDetails)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}