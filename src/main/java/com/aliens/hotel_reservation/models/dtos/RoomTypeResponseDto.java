package com.aliens.hotel_reservation.models.dtos;

import com.aliens.hotel_reservation.models.enums.RoomCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomTypeResponseDto {
    private Long id;
    private RoomCategory name;
    private Double basePrice;
    private Short capacity;
    private Short totalRooms;
    private Long hotelId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    double priceAfterApplySeasonalPrice;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    double totalPrice;

}
