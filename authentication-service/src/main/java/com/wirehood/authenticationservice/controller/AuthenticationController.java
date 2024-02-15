package com.wirehood.authenticationservice.controller;

import com.wirehood.authenticationservice.dto.LoginRequest;
import com.wirehood.authenticationservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("api/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping
    public Mono<ResponseEntity<String>> authenticateUser(@RequestBody LoginRequest loginRequest) {

        return authenticationService.authenticateUser(loginRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
