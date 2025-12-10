package com.aliens.hotel_reservation.models.dtos;

import java.time.LocalDate;

public record SeasonalPriceResponseDto(
        Long id,
        LocalDate fromDate,
        LocalDate toDate,
        Double multiplier
) {
}
