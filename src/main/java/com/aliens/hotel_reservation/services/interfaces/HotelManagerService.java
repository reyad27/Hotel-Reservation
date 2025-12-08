package com.aliens.hotel_reservation.services.interfaces;

import com.aliens.hotel_reservation.models.dtos.HotelManagerCreationDto;
import com.aliens.hotel_reservation.models.dtos.HotelManagerDto;

public interface HotelManagerService {
    HotelManagerDto getManagerById(long id);

    HotelManagerDto addNewManager(HotelManagerCreationDto dto);

    void deleteManagerById(long id);

}
