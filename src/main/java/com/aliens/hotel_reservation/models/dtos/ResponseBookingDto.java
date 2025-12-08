package com.aliens.hotel_reservation.models.dtos;

import com.aliens.hotel_reservation.models.enums.BookingStatus;
import com.aliens.hotel_reservation.models.enums.RoomCategory;

import java.time.LocalDate;

public record ResponseBookingDto(
        String hotelName,
        RoomCategory roomType,
        String roomNumber,
        LocalDate startDate,
        LocalDate endDate,
        BookingStatus status,
        double totalPrice
) {
}
