package com.aliens.hotel_reservation.models.dtos;

import com.aliens.hotel_reservation.models.entities.SeasonalPrice;
import com.aliens.hotel_reservation.models.enums.RoomCategory;
import com.aliens.hotel_reservation.models.enums.RoomStatus;

public record RoomTypeResponseDto(
          Long id,
          RoomCategory name,
          Double basePrice,
          Short capacity,
          Short totalRooms,
          Long hotelId,
          String hotelName,
          Long seasonId
) {
}
