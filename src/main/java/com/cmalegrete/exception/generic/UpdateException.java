package com.cmalegrete.exception.generic;

// 400 - Bad Request
public class UpdateException extends IllegalArgumentException {

    public UpdateException(String message) {
        super(message);
    }
    
}
