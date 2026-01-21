package com.netflix.exception;
//Add your annotations here
public class EmailSendingException extends RuntimeException{

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
