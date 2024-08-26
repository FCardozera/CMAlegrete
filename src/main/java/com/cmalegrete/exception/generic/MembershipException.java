package com.cmalegrete.exception.generic;

// 400 - Bad Request
public class MembershipException extends IllegalArgumentException {

    public MembershipException(String message) {
        super(message);
    }
    
}
