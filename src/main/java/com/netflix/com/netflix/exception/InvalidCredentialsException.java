package com.netflix.exception;
//Add your annotations here
public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
