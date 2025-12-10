package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.models.dtos.*;
import com.aliens.hotel_reservation.services.interfaces.RoomTypeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @PostMapping
    public ResponseEntity<RoomTypeResponseDto> create(@Valid @RequestBody RoomTypeRequestDto dto) {
        RoomTypeResponseDto roomTypeResponseDto = roomTypeService.create(dto);
        return new ResponseEntity<>(roomTypeResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/seasonal-price")
    public SeasonalPriceResponseDto addSeasonalPrice(@Valid @RequestBody SeasonalPriceRequestDto dto) {
        return roomTypeService.addSeasonalPrice(dto);
    }

    @GetMapping("/seasonal-prices")
    public List<SeasonalPriceResponseDto> getAllSeasonalPrices() {
        return roomTypeService.getAllSeasonalPrices();
    }

}
