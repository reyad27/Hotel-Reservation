package com.aliens.hotel_reservation.services.implementaions;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.mappers.RoomTypesMapper;
import com.aliens.hotel_reservation.mappers.HotelMapper;
import com.aliens.hotel_reservation.models.dtos.BookingDto;
import com.aliens.hotel_reservation.models.dtos.HotelRequestDto;
import com.aliens.hotel_reservation.models.dtos.HotelSearchResponseDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeDto;
import com.aliens.hotel_reservation.models.entities.*;
import com.aliens.hotel_reservation.models.enums.RoomStatus;
import com.aliens.hotel_reservation.repositories.*;
import com.aliens.hotel_reservation.services.interfaces.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.Collections.max;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    private final BookingRepository bookingRepository;

    private final RoomRepository roomRepository;

    private final SeasonalPriceRepository seasonalPriceRepository;

    private final RoomTypeRepository roomTypeRepository;

    private final RoomTypesMapper mapper;
    private final HotelMapper hotelMapper;


    @Override
    public Page<HotelSearchResponseDto> searchHotel(String city, LocalDate from, LocalDate to, short guests, Pageable pageable) {
        if (from.isAfter(to)) {
            throw new HotelBusinessException("Check-in date cannot be after check-out date");
        }

        List<Hotel> hotels = hotelRepository.findByCityIgnoreCase(city);

        if (hotels.isEmpty()) {
            throw new HotelBusinessException("No hotels found in city: " + city);
        }

        List<HotelSearchResponseDto> hotelResponseList = new ArrayList<>();

        for (Hotel hotel : hotels) {

            List<RoomType> roomTypes = roomTypeRepository.findByHotelIdAndCapacity(hotel.getId(), guests);

            if (roomTypes.isEmpty()) {
                throw new HotelBusinessException("No room types available for " + guests + " guests in hotel: " + hotel.getName());
            }

            List<RoomTypeDto> roomTypeDtos = new ArrayList<>();

            for (RoomType roomType : roomTypes) {

                if(findAvailableRoom(roomType.getId(),from,to).isEmpty()) continue;

                double totalPrice = getTotalPriceAndApplySeasonalPriceIfExist(roomType, from, to);
                RoomTypeDto dto = mapper.mapRoomTypeWithPrice(roomType, totalPrice);
                roomTypeDtos.add(dto);
            }

            if (!roomTypeDtos.isEmpty()) {
                HotelSearchResponseDto hotelDto = hotelMapper.mapHotelWithRoomTypes(hotel, roomTypeDtos);
                hotelResponseList.add(hotelDto);
            }
        }

        if (hotelResponseList.isEmpty()) {
            throw new HotelBusinessException("No available rooms found for the selected dates");
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), hotelResponseList.size());

        List<HotelSearchResponseDto> pagedList = hotelResponseList.subList(start, end);

        return new PageImpl<>(pagedList, pageable, hotelResponseList.size());

    }

    @Override
    public HotelRequestDto insertHotel(HotelRequestDto hotelDto) {
        Hotel hotel = hotelMapper.hotelRequestDtoToHotel(hotelDto);
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.hotelToHotelRequestDto(savedHotel);
    }

    @Override
    public void deleteHotelById(Long id) {
        boolean exists = hotelRepository.existsById(id);
        if (!exists) {
            throw new HotelBusinessException("Cannot delete hotel. Hotel with ID " + id + " does not exist.");
        }
        hotelRepository.deleteById(id);
    }

    private double getTotalPriceAndApplySeasonalPriceIfExist(RoomType roomType, LocalDate startDate, LocalDate endDate) {

        long diffInDays = ChronoUnit.DAYS.between(startDate, endDate);
        double totalPice = diffInDays * roomType.getBasePrice();

        SeasonalPrice seasonalPrice = seasonalPriceRepository.findById(roomType.getSeasonalPrice().getId()).orElse(null);

        if (seasonalPrice != null)
            if (!startDate.isBefore(seasonalPrice.getFromDate()) && !endDate.isAfter(seasonalPrice.getToDate()))
                totalPice *= seasonalPrice.getMultiplier();

        return totalPice;
    }


    private Optional<Room> findAvailableRoom(Long roomTypeId, LocalDate startDate, LocalDate endDate){

        List<Room> rooms = roomRepository.findAllByRoomTypeIdAndStatus(roomTypeId, RoomStatus.ACTIVE);
        List<Booking> bookings = bookingRepository.findAllByRoomTypeIdAndEndDateGreaterThanEqual(roomTypeId,LocalDate.now());

        for(Room room: rooms){

            boolean available = true;
            List<Booking> bookingsFilteredBasedOnRoomId = bookings.stream()
                    .filter(booking -> booking.getRoom().getId() == room.getId()).toList();

            for(Booking booking: bookingsFilteredBasedOnRoomId)
                if (booking.getStartDate().isBefore(endDate) && startDate.isBefore(booking.getEndDate())) {
                    available = false;
                    break;
                }

            if(available)
                return Optional.of(room);
        }

        return Optional.empty();
    }


}
