package com.aliens.hotel_reservation.models.dtos;

import jakarta.validation.constraints.NotNull;

public record HotelManagerDto(
        long id ,
        @NotNull(message = "UserId is required")
        String userId ,
        @NotNull(message = "HotelId is required")
        String hotelId
) {
}
