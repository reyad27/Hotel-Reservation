package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(field, message);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Validation failed");
        response.put("fields", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HotelBusinessException.class)
    public ResponseEntity<?> handleTransactionBusinessException(HotelBusinessException ex) {
        log.warn("Business rule violated: {}", ex.getMessage());
        return ResponseEntity
                .unprocessableEntity()
                .body(Map.of(
                        "status", HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "error", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                ));
    }
}

