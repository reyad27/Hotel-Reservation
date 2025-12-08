package com.aliens.hotel_reservation.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record HotelSearchResponseDto (
         Long hotelId,
         String name,
         String city,
         List<RoomTypeDto> roomTypes

){
}
