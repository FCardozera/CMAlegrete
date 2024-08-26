package com.cmalegrete.exception.generic;

// 401 - Unauthorized
public class UnauthorizedUserException extends RuntimeException {

    public UnauthorizedUserException(String message) {
        super(message);
    }
    
}
