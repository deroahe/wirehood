package com.wirehood.authenticationservice.controller;

import com.wirehood.authenticationservice.dto.UserDto;
import com.wirehood.authenticationservice.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/register")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public Mono<ResponseEntity<UserDto>> registerUser(@RequestBody UserDto user) {

        return userService.saveUser(user)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PutMapping
    public Mono<ResponseEntity<UserDto>> updateUser(@RequestBody UserDto user) {

        return userService.saveUser(user)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }
}
