package com.wirehood.authenticationservice.exception;

public class UnauthorizedException extends RuntimeException implements WirehoodException {

    public UnauthorizedException(String errorMessage) {
        super(errorMessage);
    }
}
