package com.aliens.hotel_reservation.models.dtos;

import java.util.List;

public record HotelSearchResponseDto (
         Long id,
         String name,
         String city,
         List<RoomTypeResponseDto> roomTypes

){
}
