package com.paypal.user_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException badRequestException) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(badRequestException.getMessage())
                .timeStamp(Instant.now())
                .build();

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException runtimeException) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(runtimeException.getMessage())
                .build();

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException exception) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,exception.getResourceName()+" with id "+exception.getResourceId()+" not found");
        log.error(apiError.toString(), exception);
        return ResponseEntity.status(apiError.status()).body(apiError);
    }
}
