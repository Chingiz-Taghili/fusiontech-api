package com.fusiontech.api.exceptions;

import com.fusiontech.api.payloads.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //Xəta mesajı düzəltmək üçün metod
    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus status) {
        //System.err.println(e.getMessage());
        logger.error("\u001B[31mXəta baş verdi: {}\u001B[0m", e.getMessage());
        ErrorResponse response = new ErrorResponse(e.getMessage(), status.value());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(ResourceAlreadyExistsException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
