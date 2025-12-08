package com.aliens.hotel_reservation.models.dtos;

import com.aliens.hotel_reservation.models.enums.RoomCategory;
import com.aliens.hotel_reservation.models.enums.RoomStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoomTypeRequestDto(
        @NotNull(message = "name of room type is required")
        RoomCategory name,
        @NotNull
        @Positive(message = "price must be greater than 0")
        Double basePrice,
        @NotNull
        @Min(value = 1 , message = "Capacity must be at least 1")
        Short capacity,
        @NotNull(message = "Total rooms is required")
        @Min(value = 1, message = "Total rooms must be at least 1")
        Short totalRooms,
        @NotNull(message = "Hotel id is required")
        Long hotelId
) {
}
