package com.aliens.hotel_reservation.services.interfaces;

import com.aliens.hotel_reservation.models.dtos.HotelRequestDto;
import com.aliens.hotel_reservation.models.dtos.HotelSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;

public interface HotelService {


    Page<HotelSearchResponseDto> searchHotel(String city, LocalDate from, LocalDate to, short guests, Pageable pageable);

    HotelRequestDto insertHotel(HotelRequestDto hotelDto);

    void deleteHotelById(Long id);
}
