package com.aliens.hotel_reservation.mappers;

import com.aliens.hotel_reservation.models.dtos.RoomDto;
import com.aliens.hotel_reservation.models.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "roomTypeId", source = "roomType.id")
    RoomDto toRoomDto(Room room);

    @Mapping(target = "roomType",ignore = true)
    @Mapping(target = "id", ignore = true)
    Room toRoomEntity(RoomDto roomDto);
}
