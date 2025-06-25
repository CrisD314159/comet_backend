package com.crisd.comet.exceptionHandling.advices;

import com.crisd.comet.exceptionHandling.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseMessage> exceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())
        );
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseMessage> badRequestHandler(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage())
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseMessage> entityNotFoundHandler(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseMessage(HttpStatus.NOT_FOUND.value(), e.getMessage())
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseMessage> unauthorizedHandler(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponseMessage(HttpStatus.UNAUTHORIZED.value(), e.getMessage())
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseMessage> validationHandler(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage())
        );
    }

}