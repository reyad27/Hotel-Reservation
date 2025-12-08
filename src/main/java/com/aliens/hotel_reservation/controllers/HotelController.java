package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.models.dtos.HotelRequestDto;
import com.aliens.hotel_reservation.models.dtos.HotelSearchResponseDto;
import com.aliens.hotel_reservation.models.entities.Hotel;
import com.aliens.hotel_reservation.services.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hotels")
public class HotelController {


    private final  HotelService hotelService;


    @GetMapping("/search")
    public ResponseEntity<Page<HotelSearchResponseDto>> searchHotels(
            @RequestParam @NotBlank String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam @Min(1) int guest,
            @PageableDefault(size = 10) Pageable pageable
    ) {

        Page<HotelSearchResponseDto> hotels = hotelService.searchHotel(city, from, to, guest, pageable);
        return ResponseEntity.ok(hotels);
    }
    @PostMapping
    public ResponseEntity<HotelRequestDto > insertHotel(@Valid @RequestBody HotelRequestDto hotelDto){

        HotelRequestDto hotelRequestDto=hotelService.insertHotel(hotelDto);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(hotelRequestDto);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotelById(@PathVariable Long id){

        hotelService.deleteHotelById(id);
        return ResponseEntity.ok("Hotel with id "+id+" deleted successfully");

    }

}
