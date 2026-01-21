package com.netflix.exception;
//Add your annotations here
public class AccountDeactivatedException extends RuntimeException{

    public AccountDeactivatedException(String message) {
        super(message);
    }
}
