package com.aliens.hotel_reservation.mappers;

import com.aliens.hotel_reservation.models.dtos.HotelRequestDto;
import com.aliens.hotel_reservation.models.dtos.HotelSearchResponseDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeDto;
import com.aliens.hotel_reservation.models.entities.Hotel;
import com.aliens.hotel_reservation.models.entities.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface HotelMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "address", target = "address")
    HotelRequestDto hotelToHotelRequestDto(Hotel hotel);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "address", target = "address")
    Hotel hotelRequestDtoToHotel(HotelRequestDto dto);


    HotelSearchResponseDto hotelToHotelSearchResponseDto(Hotel hotel);

    default HotelSearchResponseDto mapHotelWithRoomTypes(Hotel hotel, List<RoomTypeDto> roomTypeDtos) {
        return new HotelSearchResponseDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getCity(),
                roomTypeDtos
        );
    }

    default List<HotelSearchResponseDto> hotelsToHotelDtos(List<Hotel> hotels) {
        return hotels.stream()
                .map(this::hotelToHotelSearchResponseDto)
                .toList();
    }

}
