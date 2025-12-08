package com.aliens.hotel_reservation.mappers;

import com.aliens.hotel_reservation.models.dtos.SeasonalPriceRequestDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceResponseDto;
import com.aliens.hotel_reservation.models.entities.SeasonalPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeasonalPriceMapper {
    @Mapping(source = "startDate", target = "fromDate")
    @Mapping(source = "endDate", target = "toDate")
    @Mapping(source = "price", target = "multiplier")
    SeasonalPrice toEntity(SeasonalPriceRequestDto dto);

    // Entity -> ResponseDto
    @Mapping(source = "fromDate",   target = "startDate")
    @Mapping(source = "toDate",     target = "endDate")
    @Mapping(source = "multiplier", target = "price")
    SeasonalPriceResponseDto toDto(SeasonalPrice entity);
}
