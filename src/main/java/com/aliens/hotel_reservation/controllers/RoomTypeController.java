package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.models.dtos.RoomTypeRequestDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeResponseDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceRequestDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceResponseDto;
import com.aliens.hotel_reservation.services.interfaces.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/room-types")
@RequiredArgsConstructor
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @PostMapping
    public ResponseEntity<RoomTypeResponseDto> create(@Valid @RequestBody RoomTypeRequestDto dto) {
        RoomTypeResponseDto roomTypeResponseDto = roomTypeService.create(dto);
        return new ResponseEntity<>(roomTypeResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/seasonal-prices")
    public ResponseEntity<SeasonalPriceResponseDto> addSeasonalPrice(@Valid @RequestBody SeasonalPriceRequestDto dto) {
        SeasonalPriceResponseDto seasonalPriceResponseDto = roomTypeService.addSeasonalPrice(dto);
        return new ResponseEntity<>(seasonalPriceResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/seasonal-prices/{roomTypeId}")
    public ResponseEntity<List<SeasonalPriceResponseDto>> getAllSeasonalPrices(@PathVariable Long roomTypeId) {
        List<SeasonalPriceResponseDto> seasonalPriceResponsesDto = roomTypeService.getAllSeasonalPrices(roomTypeId);
        return new ResponseEntity<>(seasonalPriceResponsesDto, HttpStatus.OK);
    }

}
