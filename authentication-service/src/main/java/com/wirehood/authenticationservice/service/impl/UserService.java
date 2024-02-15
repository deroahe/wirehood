package com.wirehood.authenticationservice.service.impl;

import com.wirehood.authenticationservice.dto.UserDto;
import com.wirehood.authenticationservice.exception.UserNotFoundException;
import com.wirehood.authenticationservice.entity.User;
import com.wirehood.authenticationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static reactor.core.scheduler.Schedulers.boundedElastic;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Mono<UserDto> saveUser(UserDto userDto) {

        final var user = convertUserDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return Mono.fromCallable(() -> userRepository.save(user))
                .map(this::convertUserToUserDto)
                .subscribeOn(boundedElastic());
    }

    public Mono<UserDto> getUserByUsernameAndPassword(String username, String password) {

        return Mono.fromCallable(() -> Optional.of(userRepository.findByUsernameAndPassword(username, password))
                        .orElseThrow(() -> new UserNotFoundException("User not found")))
                .map(this::convertUserToUserDto);
    }

    private User convertUserDtoToUser(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .build();
    }

    private UserDto convertUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }
}
