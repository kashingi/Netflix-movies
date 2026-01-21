package com.netflix.exception;
//Add your annotations here
public class EmailNotVerifiedException extends RuntimeException{

    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
