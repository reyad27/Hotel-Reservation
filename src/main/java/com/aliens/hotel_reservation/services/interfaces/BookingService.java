package com.aliens.hotel_reservation.services.interfaces;

import com.aliens.hotel_reservation.models.dtos.BookingDto;
import com.aliens.hotel_reservation.models.dtos.ResponseBookingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {

    ResponseBookingDto createBooking(BookingDto bookingDto);

    Page<ResponseBookingDto> getAllBookingsByGuestId(Long guestId, Pageable pageable);

    Page<ResponseBookingDto> getAllBookingsByHotelId(Long hotelId, Pageable pageable);

    ResponseBookingDto cancelBookingByGuest(Long bookingId);

    ResponseBookingDto cancelBookingByManager(Long bookingId);
}
