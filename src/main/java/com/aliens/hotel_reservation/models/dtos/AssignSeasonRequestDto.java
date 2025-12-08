package com.aliens.hotel_reservation.models.dtos;

import jakarta.validation.constraints.NotNull;

public record AssignSeasonRequestDto(
        @NotNull(message = "seasonId is required")
        Long seasonId
){}
