package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.models.dtos.AssignSeasonRequestDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeRequestDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeResponseDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceRequestDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceResponseDto;
import com.aliens.hotel_reservation.services.interfaces.RoomTypeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomtypes")
@AllArgsConstructor
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @PostMapping
    public RoomTypeResponseDto create(@Valid @RequestBody RoomTypeRequestDto dto) {
        return roomTypeService.create(dto);
    }
    @PostMapping("/{roomTypeId}/seasonal-price")
    public SeasonalPriceResponseDto addSeasonalPrice(@PathVariable Long roomTypeId,@Valid @RequestBody SeasonalPriceRequestDto dto) {
        return roomTypeService.addSeasonalPrice(roomTypeId, dto);
    }
    @PatchMapping("/{roomTypeId}/seasonal-price")
    public RoomTypeResponseDto assignSeason(
            @PathVariable Long roomTypeId,
            @Valid @RequestBody AssignSeasonRequestDto dto
    ) {
        return roomTypeService.assignSeason(roomTypeId, dto.seasonId());
    }
    @GetMapping("/seasonal-prices")
    public List<SeasonalPriceResponseDto> getAllSeasonalPrices() {
        return roomTypeService.getAllSeasonalPrices();
    }
}
