package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.models.dtos.HotelRequestDto;
import com.aliens.hotel_reservation.models.dtos.HotelSearchResponseDto;
import com.aliens.hotel_reservation.services.interfaces.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hotels")
public class HotelController {

    private final  HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelSearchResponseDto>> searchHotels(
            @RequestParam @NotBlank String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam @Min(1) short guests,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<HotelSearchResponseDto> hotels = hotelService.searchHotel(city, from, to, guests, pageable);
        return ResponseEntity.ok(hotels);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelRequestDto > insertHotel(@Valid @RequestBody HotelRequestDto hotelDto){
        HotelRequestDto hotelRequestDto=hotelService.insertHotel(hotelDto);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(hotelRequestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotelById(@PathVariable Long id){
        hotelService.deleteHotelById(id);
        return ResponseEntity.ok("Hotel with id "+id+" deleted successfully");
    }

}
