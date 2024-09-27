package com.cmalegrete.exception.generic;

// 400 - Bad Request
public class UserNotExistsException extends RuntimeException {
    
    public UserNotExistsException(String message) {
        super(message);
    }

}