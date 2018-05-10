package com.meetup.meetup.exception;

import org.springframework.context.annotation.PropertySource;

import static java.lang.String.format;

public class FailedToLoginException extends RuntimeException {
    public FailedToLoginException(String username) {
        super(format("SendCustomErrorFailed to login with username %s", username));
    }
}