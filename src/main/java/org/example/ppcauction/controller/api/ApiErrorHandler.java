package org.example.ppcauction.controller.api;

import org.example.ppcauction.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice(basePackages="org.example.ppcauction.controller.api")
public class ApiErrorHandler {
    public record ApiError(int status,String message,LocalDateTime timestamp) { }
    @ExceptionHandler(ResourceNotFoundException.class) public ResponseEntity<ApiError> notFound(ResourceNotFoundException ex){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(404,ex.getMessage(),LocalDateTime.now()));}
    @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<ApiError> invalid(IllegalArgumentException ex){return ResponseEntity.badRequest().body(new ApiError(400,ex.getMessage(),LocalDateTime.now()));}
}
