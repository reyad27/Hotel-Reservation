package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.models.dtos.BookingDto;
import com.aliens.hotel_reservation.models.dtos.ResponseBookingDto;
import com.aliens.hotel_reservation.services.interfaces.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ResponseBookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        ResponseBookingDto responseBookingDto = bookingService.createBooking(bookingDto);
        return new ResponseEntity<>(responseBookingDto, HttpStatus.CREATED);
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<Page<ResponseBookingDto>> getAllBookingsByGuestId(@PathVariable Long guestId, Pageable pageable) {
        Page<ResponseBookingDto> responseBookingsDto = bookingService.getAllBookingsByGuestId(guestId, pageable);
        return new ResponseEntity<>(responseBookingsDto, HttpStatus.OK);
    }

    @GetMapping("/manager/{hotelId}")
    public ResponseEntity<Page<ResponseBookingDto>> getAllBookingsByHotelId(@PathVariable Long hotelId, Pageable pageable){
        Page<ResponseBookingDto> responseBookingsDto = bookingService.getAllBookingsByHotelId(hotelId, pageable);
        return new ResponseEntity<>(responseBookingsDto, HttpStatus.OK);
    }

    @PatchMapping("/guest/{bookingId}")
    public ResponseEntity<ResponseBookingDto> cancelBookingByGuest(@PathVariable Long bookingId){
        ResponseBookingDto responseBookingDto = bookingService.cancelBookingByGuest(bookingId);
        return new ResponseEntity<>(responseBookingDto,HttpStatus.OK);//204??
    }

    @PatchMapping("/manager/{bookingId}")
    public ResponseEntity<ResponseBookingDto> cancelBookingByManager(@PathVariable Long bookingId){
        ResponseBookingDto responseBookingDto = bookingService.cancelBookingByManager(bookingId);
        return new ResponseEntity<>(responseBookingDto,HttpStatus.OK);
    }
}
