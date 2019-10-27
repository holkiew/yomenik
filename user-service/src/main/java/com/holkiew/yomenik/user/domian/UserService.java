package com.holkiew.yomenik.user.domian;

import com.holkiew.yomenik.user.domian.dto.NewUserRequest;
import com.holkiew.yomenik.user.domian.entity.Player;
import com.holkiew.yomenik.user.domian.entity.User;
import com.holkiew.yomenik.user.domian.port.UserRepository;
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


    public Mono<Player> getUserByUsernameOrId(String id, String username) {
        if (Objects.nonNull(id)) {
            return userRepository.findById(id);
        } else if (Objects.nonNull(username)) {
            return userRepository.findByUsername(username);
        } else {
            return Mono.error(new RuntimeException("User not found"));
        }
    }

    public Mono<Player> saveNewUser(NewUserRequest request) {
        Player user = (Player) mapper.map(request, User.class);
        user.setId(Objects.nonNull(user.getId()) ? user.getId() : User.createUniqueId());
        user.setFleetConfigId(user.getId());
        user.setResearchId(user.getId());
        return userRepository.save(user);
    }
}
