package com.cmalegrete.exception.generic;

// 400 - Bad Request
public class EmailAlreadyRegisteredException extends RuntimeException {
    
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }

}