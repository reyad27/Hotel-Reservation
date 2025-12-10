package com.aliens.hotel_reservation.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record SeasonalPriceRequestDto (

        @NotNull
        Long roomTypeId,

        @NotNull(message = "fromDate is required")
        LocalDate fromDate,

        @NotNull(message = "toDate is required")
        LocalDate toDate,

        @NotNull(message = "multiplier is required")
        @Positive(message = "multiplier must be > 0")
        Double multiplier
){
}
