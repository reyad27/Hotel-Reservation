package com.aliens.hotel_reservation.mappers;

import com.aliens.hotel_reservation.models.dtos.HotelRequestDto;
import com.aliens.hotel_reservation.models.dtos.HotelSearchResponseDto;
import com.aliens.hotel_reservation.models.entities.Hotel;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface HotelMapper {

    HotelRequestDto hotelToHotelRequestDto(Hotel hotel);


    Hotel hotelRequestDtoToHotel(HotelRequestDto dto);

    HotelSearchResponseDto hotelToHotelSearchResponseDto(Hotel hotel);


}
