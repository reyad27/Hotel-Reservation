package com.aliens.hotel_reservation.models.dtos;

import java.time.LocalDate;

public record SeasonalPriceResponseDto(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Double price
) {
}
