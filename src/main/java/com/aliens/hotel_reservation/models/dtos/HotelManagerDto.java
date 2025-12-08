package com.aliens.hotel_reservation.models.dtos;

import com.aliens.hotel_reservation.models.entities.Hotel;
import com.aliens.hotel_reservation.models.entities.User;

public record HotelManagerDto(long id , User user , Hotel hotel) {

}
