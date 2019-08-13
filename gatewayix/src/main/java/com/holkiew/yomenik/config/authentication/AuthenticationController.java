package com.holkiew.yomenik.config.authentication;

import com.holkiew.yomenik.config.authentication.dto.AuthRequest;
import com.holkiew.yomenik.config.authentication.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@RequestMapping
@Log4j2
public class AuthenticationController {

    private final JWTUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest request) {
        return userRepository.findByUsername(request.getUsername()).map((userDetails) -> {
            System.out.println("LAPIOE");
            System.out.println("LAPIOE");
            System.out.println("LAPIOE");
            System.out.println("LAPIOE");
            System.out.println("LAPIOE");
            System.out.println("LAPIOE");
            if (passwordEncoder.encode(request.getPassword()).equals(userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(jwtUtils.generateToken(userDetails)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}