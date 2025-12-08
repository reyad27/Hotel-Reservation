package com.aliens.hotel_reservation.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingDto(
        @NotNull(message = "GuestId must not be null")
        Long guestId,
        @NotNull(message = "HotelId must not be null")
        Long hotelId,
        @NotNull(message = "RoomTypeId must not be null")
        Long roomTypeId,
        @NotNull(message = "StartDate must not be null")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @NotNull(message = "EndDate must not be null")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,
        @Min(1)
        short guests
) {
    @AssertTrue(message = "startDate must be before endDate")
    public boolean isValidDates() {
        return startDate.isBefore(endDate);
    }
}
