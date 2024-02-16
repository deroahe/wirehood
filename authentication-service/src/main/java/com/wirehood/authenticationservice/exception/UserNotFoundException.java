package com.wirehood.authenticationservice.exception;

public class UserNotFoundException extends RuntimeException implements WirehoodException {

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
