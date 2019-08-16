package com.holkiew.yomenik.user.service;

import com.holkiew.yomenik.user.service.dto.NewUserRequest;
import com.holkiew.yomenik.user.service.entity.User;
import com.holkiew.yomenik.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final ModelMapper mapper;

    public Mono<User> getUserByUsernameOrId(String id, String username) {
        if (Objects.nonNull(id)) {
            return userRepository.findById(id);
        } else if (Objects.nonNull(username)) {
            return userRepository.findByUsername(username);
        } else {
            return Mono.error(new Exception("User not found"));
        }
    }

    public Mono<User> saveNewUser(NewUserRequest request) {
        User user = mapper.map(request, User.class);
        return userRepository.save(user);
    }
}
