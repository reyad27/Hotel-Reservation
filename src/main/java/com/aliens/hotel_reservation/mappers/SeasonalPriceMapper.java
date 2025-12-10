package com.aliens.hotel_reservation.mappers;

import com.aliens.hotel_reservation.models.dtos.SeasonalPriceRequestDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceResponseDto;
import com.aliens.hotel_reservation.models.entities.SeasonalPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeasonalPriceMapper {

    @Mapping(target = "roomType", ignore = true)
    SeasonalPrice toSeasonalPriceEntity(SeasonalPriceRequestDto dto);

    @Mapping(target = "roomTypeId", source = "roomType.id")
    SeasonalPriceResponseDto toResponseDto(SeasonalPrice seasonalPrice);
}
