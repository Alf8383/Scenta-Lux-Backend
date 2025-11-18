package com.scentalux.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.lang.NonNull; // Import NonNull annotation
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorRecord> handleDefaultExceptions(Exception ex, WebRequest request) {
        CustomErrorRecord err = new CustomErrorRecord(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<CustomErrorRecord> handleModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
        CustomErrorRecord err = new CustomErrorRecord(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<CustomErrorRecord> handleArithmeticException(ArithmeticException ex, WebRequest request) {
        CustomErrorRecord err = new CustomErrorRecord(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex, 
            @NonNull HttpHeaders headers, 
            @NonNull HttpStatusCode status, 
            @NonNull WebRequest request) {

        // Extracting field validation errors
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField().concat(":").concat(err.getDefaultMessage()))
                .collect(Collectors.joining(","));

        // Creating a custom error record
        CustomErrorRecord err = new CustomErrorRecord(LocalDateTime.now(), msg, request.getDescription(false));

        // Returning a ResponseEntity<Object> to comply with the superclass contract
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
