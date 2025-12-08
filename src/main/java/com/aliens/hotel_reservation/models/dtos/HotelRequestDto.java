package com.aliens.hotel_reservation.models.dtos;

import jakarta.validation.constraints.NotBlank;

import java.sql.Date;

public record HotelRequestDto(
        @NotBlank(message = "Hotel name is required")
        String name,
        @NotBlank(message = "City is required")
        String city,
        @NotBlank(message = "Address is required")
        String address

) {
}
