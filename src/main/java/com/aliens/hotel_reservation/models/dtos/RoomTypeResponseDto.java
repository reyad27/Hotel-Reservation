package com.aliens.hotel_reservation.models.dtos;

import com.aliens.hotel_reservation.models.enums.RoomCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomTypeResponseDto{
          private Long id;
          private RoomCategory name;
          private Double basePrice;
          private Short capacity;
          private Short totalRooms;
          private Long hotelId;
          double priceAfterApplySeasonalPrice;

}
