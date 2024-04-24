package com.favoriteSongBackend.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleBadRequestException(ResponseStatusException ex) {
        // if you want you can do some extra processing with message and status of an exception
        // or you can return it without any processing like this:
        return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
    }
}
