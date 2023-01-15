package com.neobis.rentit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class AttributeNotInCategoryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AttributeNotInCategoryException(String message){
        super(message);
    }
}