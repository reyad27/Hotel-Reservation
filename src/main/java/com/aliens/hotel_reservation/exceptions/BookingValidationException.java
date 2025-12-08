package com.aliens.hotel_reservation.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class BookingValidationException extends RuntimeException {

    private final List<String> errors;

    public BookingValidationException(List<String> errors) {
        super("Validation errors occurred during booking processing.");
        this.errors = errors;
    }
}
