package com.aliens.hotel_reservation.models.dtos;

public record HotelManagerCreationDto(
        String email,
        String username,
        String password,
        Long hotelId
) {
}
