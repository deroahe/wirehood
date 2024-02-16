package com.wirehood.authenticationservice.controller;

import com.wirehood.authenticationservice.dto.UserDto;
import com.wirehood.authenticationservice.exception.UserNotFoundException;
import com.wirehood.authenticationservice.exception.WirehoodException;
import com.wirehood.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static java.util.function.Predicate.not;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.notFound;
import static reactor.core.publisher.Mono.just;

@RestController
@RequestMapping("api/authentication/register")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Mono<ResponseEntity<UserDto>> registerUser(@RequestBody UserDto user) {

        return userService.createUser(user)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> just(badRequest().build()));
    }

    @PutMapping
    public Mono<ResponseEntity<UserDto>> updateUser(@RequestBody UserDto user) {

        return userService.updateUser(user)
                .map(ResponseEntity::ok)
                .onErrorResume(UserNotFoundException.class, e -> just(notFound().build()))
                .onErrorResume(not(WirehoodException.class::isInstance), e -> just(badRequest().build()));
    }
}
