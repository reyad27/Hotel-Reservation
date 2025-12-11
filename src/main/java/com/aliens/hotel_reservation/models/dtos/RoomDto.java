package com.aliens.hotel_reservation.models.dtos;

import com.aliens.hotel_reservation.models.enums.RoomStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomDto(

        Long id,

        @NotNull(message = "RoomTypeId is required")
        Long roomTypeId,

        @NotBlank(message = "RoomNumber is required")
        String roomNumber,

        RoomStatus status
) {
}
