package com.aliens.hotel_reservation.mappers;

import com.aliens.hotel_reservation.models.dtos.HotelManagerDto;
import com.aliens.hotel_reservation.models.entities.HotelManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel="spring")
public interface HotelManagerMapper {
    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "user.id", target = "userId")
    HotelManagerDto hotelManagerToHotelManagerDto(HotelManager hotelManager);
}
