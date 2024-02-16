package com.wirehood.authenticationservice.controller;

import com.wirehood.authenticationservice.dto.LoginRequest;
import com.wirehood.authenticationservice.exception.UnauthorizedException;
import com.wirehood.authenticationservice.exception.UserNotFoundException;
import com.wirehood.authenticationservice.exception.WirehoodException;
import com.wirehood.authenticationservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static java.util.function.Predicate.not;
import static org.springframework.http.ResponseEntity.*;
import static reactor.core.publisher.Mono.just;

@RestController
@RequestMapping("api/authentication/login")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public Mono<ResponseEntity<String>> authenticateUser(@RequestBody LoginRequest loginRequest) {

        return authenticationService.authenticateUser(loginRequest)
                .map(token -> ok().body(token))
                .onErrorResume(UnauthorizedException.class, e -> just(status(401).body(e.getMessage())))
                .onErrorResume(UserNotFoundException.class, e -> just(status(404).body(e.getMessage())))
                .onErrorResume(not(WirehoodException.class::isInstance), e -> just(status(400).body("Something went wrong")));
    }
}
