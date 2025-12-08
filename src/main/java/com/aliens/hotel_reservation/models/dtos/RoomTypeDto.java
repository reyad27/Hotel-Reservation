package com.aliens.hotel_reservation.models.dtos;

import com.aliens.hotel_reservation.models.enums.RoomCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


public record RoomTypeDto(
        @NotNull(message = "Room type ID cannot be null")
        Long id,
        @NotNull(message = "Room category cannot be null")
        RoomCategory name,
        @Min(value = 1, message = "Capacity must be at least 1")
        int capacity,
        @Positive(message = "Total price must be greater than 0")
        double totalPrice
       ) {

}

