package com.wirehood.authenticationservice.dto;

import com.wirehood.authenticationservice.security.Role;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class UserDto {

    String id;
    String username;
    String password;
    List<Role> roles;
}
