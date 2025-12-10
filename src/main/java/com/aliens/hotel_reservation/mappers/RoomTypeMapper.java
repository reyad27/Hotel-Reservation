package com.aliens.hotel_reservation.mappers;

import com.aliens.hotel_reservation.models.dtos.RoomTypeRequestDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeResponseDto;
import com.aliens.hotel_reservation.models.entities.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(target = "priceAfterApplySeasonalPrice", ignore = true)
    RoomTypeResponseDto toResponseDto(RoomType roomType);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    RoomType toRoomTypeEntity(RoomTypeRequestDto dto);
}
