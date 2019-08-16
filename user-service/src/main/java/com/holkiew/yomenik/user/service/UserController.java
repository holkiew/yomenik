package com.holkiew.yomenik.user.service;

import com.holkiew.yomenik.user.service.dto.NewUserRequest;
import com.holkiew.yomenik.user.service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public Mono<User> getUserByUsernameOrId(@RequestParam(required = false) String id,
                                            @RequestParam(required = false) String username) {
        return userService.getUserByUsernameOrId(id, username);
    }

    @PutMapping("/user")
    public Mono<User> saveNewUser(@RequestBody @Valid NewUserRequest request) {
        return userService.saveNewUser(request);
    }
}
