package com.aliens.hotel_reservation.services.interfaces;

import com.aliens.hotel_reservation.models.dtos.RoomDto;
import com.aliens.hotel_reservation.models.enums.RoomStatus;

public interface RoomService {
    RoomDto addRoom(RoomDto roomDto);

    RoomDto changeRoomStatus(Long roomId, RoomStatus roomStatus);


}
