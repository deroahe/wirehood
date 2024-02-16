package com.wirehood.authenticationservice.service;

import com.wirehood.authenticationservice.dto.LoginRequest;
import com.wirehood.authenticationservice.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;

    public Mono<String> authenticateUser(LoginRequest loginRequest) {

        return userService.getUserByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword())
                .map(jwtService::generateToken);
    }
}
