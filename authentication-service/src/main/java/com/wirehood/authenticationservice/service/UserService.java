package com.wirehood.authenticationservice.service;

import com.wirehood.authenticationservice.dto.UserDto;
import com.wirehood.authenticationservice.exception.UnauthorizedException;
import com.wirehood.authenticationservice.exception.UserNotFoundException;
import com.wirehood.authenticationservice.entity.User;
import com.wirehood.authenticationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.fromCallable;
import static reactor.core.scheduler.Schedulers.boundedElastic;

@Service
@RequiredArgsConstructor
public class UserService {

    @SuppressWarnings("java:S116")
    private final Logger LOGGER = getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Mono<UserDto> createUser(UserDto userDto) {

        final var user = convertUserDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return fromCallable(() ->
                        userRepository.save(user)
                )
                .doOnError(t -> LOGGER.error("Error while registering user '{}'", userDto.getUsername(), t))
                .map(this::convertUserToUserDto)
                .subscribeOn(boundedElastic());
    }

    public Mono<UserDto> getUserByUsernameAndPassword(String username, String password) {

        return fromCallable(() ->
                        ofNullable(userRepository.findByUsername(username))
                                .orElseThrow(() -> new UserNotFoundException("User not found"))
                )
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(this::convertUserToUserDto)
                .switchIfEmpty(error(new UnauthorizedException("Incorrect password")))
                .doOnError(t -> LOGGER.error("Error while getting user '{}'", username, t));
    }

    public Mono<UserDto> updateUser(UserDto userDto) {

        final var user = convertUserDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return fromCallable(() ->
                        ofNullable(userRepository.findByUsername(user.getUsername()))
                                .map(foundUser -> userRepository.save(user))
                                .orElseThrow(() -> new UserNotFoundException("User not found"))
                )
                .doOnError(t -> LOGGER.error("Error while updating user '{}'", userDto.getUsername(), t))
                .map(this::convertUserToUserDto)
                .subscribeOn(boundedElastic());
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
