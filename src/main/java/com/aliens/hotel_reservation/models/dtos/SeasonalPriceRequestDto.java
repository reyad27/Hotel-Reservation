package com.aliens.hotel_reservation.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record SeasonalPriceRequestDto (
        @NotNull(message = "startDate is required")
        LocalDate startDate,

        @NotNull(message = "endDate is required")
        LocalDate endDate,

        @NotNull(message = "price is required")
        @Positive(message = "price must be > 0")
        Double price
){
}
