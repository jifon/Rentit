package com.neobis.rentit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthenticatedException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public NotAuthenticatedException(String message){
        super(message);
    }
}