package com.wirehood.authenticationservice.dto;

import lombok.Value;

@Value
public class LoginRequest {

    String username;
    String password;
}
