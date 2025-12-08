package com.aliens.hotel_reservation.services.interfaces;

import com.aliens.hotel_reservation.models.dtos.RoomTypeRequestDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeResponseDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceRequestDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceResponseDto;

import java.util.List;

public interface RoomTypeService {
    RoomTypeResponseDto create(RoomTypeRequestDto  dto);

    SeasonalPriceResponseDto addSeasonalPrice(Long roomTypeId,SeasonalPriceRequestDto dto);
    RoomTypeResponseDto assignSeason(Long roomTypeId, Long seasonId);
    List<SeasonalPriceResponseDto> getAllSeasonalPrices();

}
