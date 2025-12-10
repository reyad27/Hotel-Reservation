package com.aliens.hotel_reservation.services.implementaions;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.mappers.HotelMapper;
import com.aliens.hotel_reservation.models.dtos.HotelRequestDto;
import com.aliens.hotel_reservation.models.dtos.HotelSearchResponseDto;
import com.aliens.hotel_reservation.models.entities.*;
import com.aliens.hotel_reservation.models.enums.RoomStatus;
import com.aliens.hotel_reservation.repositories.*;
import com.aliens.hotel_reservation.services.interfaces.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final SeasonalPriceRepository seasonalPriceRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final HotelMapper hotelMapper;


    @Override
    public Page<HotelSearchResponseDto> searchHotel(String city, LocalDate from, LocalDate to, short guests, Pageable pageable) {
        List<Hotel> hotels = hotelRepository.findAll();

        if (city != null)
            hotels = hotels.stream()
                    .filter(hotel -> hotel.getCity().equalsIgnoreCase(city))
                    .toList();

        return null;

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
        double totalPrice = diffInDays * roomType.getBasePrice();

        List<SeasonalPrice> seasonalPrices = seasonalPriceRepository
                .findAllByRoomTypeIdAndToDateGreaterThanEqual(roomType.getId(), LocalDate.now());

        for (SeasonalPrice seasonalPrice : seasonalPrices)
            if (!startDate.isBefore(seasonalPrice.getFromDate()) && !endDate.isAfter(seasonalPrice.getToDate()))
                totalPrice *= seasonalPrice.getMultiplier();

        return totalPrice;
    }


    private Optional<Room> findAvailableRoom(Long roomTypeId, LocalDate startDate, LocalDate endDate) {

        List<Room> rooms = roomRepository.findAllByRoomTypeIdAndStatus(roomTypeId, RoomStatus.ACTIVE);
        List<Booking> bookings = bookingRepository.findAllByRoomTypeIdAndEndDateGreaterThanEqual(roomTypeId, LocalDate.now());

        for (Room room : rooms) {

            boolean available = true;
            List<Booking> bookingsFilteredBasedOnRoomId = bookings.stream()
                    .filter(booking -> booking.getRoom().getId() == room.getId()).toList();

            for (Booking booking : bookingsFilteredBasedOnRoomId)
                if (booking.getStartDate().isBefore(endDate) && startDate.isBefore(booking.getEndDate())) {
                    available = false;
                    break;
                }

            if (available)
                return Optional.of(room);
        }

        return Optional.empty();
    }


}
