package com.aliens.hotel_reservation.mappers;

import com.aliens.hotel_reservation.models.dtos.RoomTypeDto;
import com.aliens.hotel_reservation.models.entities.RoomType;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RoomTypesMapper {


    default RoomTypeDto mapRoomTypeWithPrice(RoomType roomType, double totalPrice) {
        return new RoomTypeDto(
                roomType.getId(),
                roomType.getName(),
                roomType.getCapacity(),
                totalPrice
        );
    }


}


