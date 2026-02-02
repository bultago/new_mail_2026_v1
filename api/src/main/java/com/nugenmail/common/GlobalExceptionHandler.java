package com.nugenmail.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global Exception Handler
 * Intercepts exceptions across the application and returns standardized API
 * responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles generic exceptions.
     * 
     * @param e Exception
     * @return 500 Internal Server Error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    /**
     * Handles 404 No Handler Found exceptions.
     * 
     * @param e NoHandlerFoundException
     * @return 404 Not Found response
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoHandlerFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Endpoint not found"));
    }
}
