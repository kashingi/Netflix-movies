package com.netflix.exception;
//Add your annotations here
public class InvalidRoleException extends RuntimeException{

    public InvalidRoleException(String message) {
        super(message);
    }
}
