package com.aliens.hotel_reservation.mappers;

import com.aliens.hotel_reservation.models.dtos.ResponseBookingDto;
import com.aliens.hotel_reservation.models.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "booking.hotel.name",target = "hotelName")
    @Mapping(source = "booking.roomType.name", target = "roomType")
    @Mapping(source = "booking.room.roomNumber", target = "roomNumber")
    ResponseBookingDto fromBookingToResponse(Booking booking);
}
