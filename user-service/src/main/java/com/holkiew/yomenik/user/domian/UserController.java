package com.holkiew.yomenik.user.domian;

import com.holkiew.yomenik.user.domian.dto.NewUserRequest;
import com.holkiew.yomenik.user.domian.entity.User;
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

    @PostMapping("/user")
    public Object saveNewUser(@RequestBody @Valid NewUserRequest request) {
        return userService.saveNewUser(request);
    }

}
