package com.netflix.exception;
//Add your annotations here
public class BadCredentialsException extends RuntimeException{

    public BadCredentialsException(String message) {
        super(message);
    }
}
