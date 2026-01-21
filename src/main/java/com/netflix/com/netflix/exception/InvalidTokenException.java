package com.netflix.exception;
//Add your annotations here
public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message) {
        super(message);
    }
}
