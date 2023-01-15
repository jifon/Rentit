package com.neobis.rentit.advice;

import com.neobis.rentit.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException ex) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ErrorMessage err = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .message("Resource Not Found")
                .errors(details)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> EmailAlreadyExistsException(
            EmailAlreadyExistsException ex) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ErrorMessage err = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .message("Email already exists")
                .errors(details)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<Object> PhoneNumberAlreadyExistsException(
            PhoneNumberAlreadyExistsException ex) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ErrorMessage err = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .message("Phone Number already exists")
                .errors(details)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }


    @ExceptionHandler(EntityStillReferencedException.class)
    public ResponseEntity<Object> EntityStillReferencedException(
            EntityStillReferencedException ex) {

        List<String> details = new ArrayList<>();
        ErrorMessage err = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .errors(details)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AttributeNotInCategoryException.class)
    public ResponseEntity<Object> AttributeNotInCategoryException(
            AttributeNotInCategoryException ex) {

        List<String> details = new ArrayList<>();
        ErrorMessage err = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .errors(details)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<Object> NotAuthenticatedException(
            NotAuthenticatedException ex) {

        List<String> details = new ArrayList<>();
        ErrorMessage err = ErrorMessage.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .errors(details)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
    }



//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<Object> DataIntegrityViolationException(
//            DataIntegrityViolationException ex) {
//
//        List<String> details = new ArrayList<>();
//        ErrorMessage err = ErrorMessage.builder()
//                .statusCode(HttpStatus.BAD_REQUEST.value())
//                .timestamp(LocalDateTime.now())
//                .message(ex.getMessage())
//                .errors(details.add())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
//    }

}