package com.cmalegrete.exception.generic;

// 400 - Bad Request
public class FileException extends IllegalArgumentException {

    public FileException(String message) {
        super(message);
    }
    
}
