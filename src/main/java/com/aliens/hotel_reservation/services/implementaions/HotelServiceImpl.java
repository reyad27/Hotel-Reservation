package com.aliens.hotel_reservation.services.implementaions;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.mappers.HotelMapper;
import com.aliens.hotel_reservation.mappers.RoomTypeMapper;
import com.aliens.hotel_reservation.models.dtos.HotelRequestDto;
import com.aliens.hotel_reservation.models.dtos.HotelSearchResponseDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeResponseDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final RoomTypeMapper roomTypeMapper;


    @Override
    public Page<HotelSearchResponseDto> searchHotel(
            String city, LocalDate from, LocalDate to, short guests, Pageable pageable) {

        LocalDate validatedFrom = getOrDefaultStartDate(from);
        LocalDate validatedTo = getOrDefaultEndDate(to, validatedFrom);

        validateDates(validatedFrom, validatedTo);

        List<Hotel> hotels = hotelRepository.findByCityIgnoreCase(city);

        List<HotelSearchResponseDto> results = hotels.stream()
                .map(hotel -> buildHotelSearchResponse(hotel, validatedFrom, validatedTo, guests))
                .filter(Objects::nonNull)
                .toList();

        if (results.isEmpty()) {
            throw new HotelBusinessException("Hotels found in city but no available rooms for selected dates.");
        }

        return paginateResults(results, pageable);
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

    private double getPriceAfterApplySeasonalPrice(RoomType roomType, LocalDate startDate, LocalDate endDate) {
        double priceAfterSeasonal = 0;
        if (findAvailableRoom(roomType.getId(), startDate, endDate).isPresent()) {
            long diffInDays = ChronoUnit.DAYS.between(startDate, endDate);

            double basePrice = roomType.getBasePrice();

            List<SeasonalPrice> seasonalPrices =
                    seasonalPriceRepository.findAllByRoomTypeIdAndToDateGreaterThanEqual(roomType.getId(), LocalDate.now());

            double multiplier = 1.0;

            for (SeasonalPrice s : seasonalPrices) {
                if (!startDate.isBefore(s.getFromDate()) && !endDate.isAfter(s.getToDate())) {
                    multiplier = s.getMultiplier();
                    break;
                }
            }

            priceAfterSeasonal = basePrice * multiplier;

        }


        return priceAfterSeasonal;
    }

    private LocalDate getOrDefaultStartDate(LocalDate from) {
        return (from == null) ? LocalDate.now() : from;
    }

    private LocalDate getOrDefaultEndDate(LocalDate to, LocalDate from) {
        return (to == null) ? from.plusDays(3) : to;
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new HotelBusinessException("Start date cannot be after end date.");
        }
    }

    private HotelSearchResponseDto buildHotelSearchResponse(
            Hotel hotel, LocalDate from, LocalDate to, short guests) {

        List<RoomType> roomTypes =
                roomTypeRepository.findByHotelIdAndCapacityIsGreaterThanEqual(hotel.getId(), guests);

        List<RoomTypeResponseDto> roomTypeDtos = buildRoomTypeDtos(roomTypes, from, to);

        if (roomTypeDtos.isEmpty()) {
            return null;
        }

        HotelSearchResponseDto hotelDto = hotelMapper.hotelToHotelSearchResponseDto(hotel);

        return new HotelSearchResponseDto(
                hotelDto.id(),
                hotelDto.name(),
                hotelDto.city(),
                roomTypeDtos
        );
    }

    private List<RoomTypeResponseDto> buildRoomTypeDtos(
            List<RoomType> roomTypes, LocalDate from, LocalDate to) {

        return roomTypes.stream()
                .filter(rt -> findAvailableRoom(rt.getId(), from, to).isPresent())
                .map(rt -> mapRoomTypeWithPrices(rt, from, to))
                .toList();
    }

    private RoomTypeResponseDto mapRoomTypeWithPrices(
            RoomType roomType, LocalDate from, LocalDate to) {

        double totalPrice = getTotalPriceAndApplySeasonalPriceIfExist(roomType, from, to);

        RoomTypeResponseDto dto = roomTypeMapper.toResponseDto(roomType);
        dto.setTotalPrice(totalPrice);
        dto.setPriceAfterApplySeasonalPrice(
                getPriceAfterApplySeasonalPrice(roomType, from, to)
        );

        return dto;
    }

    private Page<HotelSearchResponseDto> paginateResults(
            List<HotelSearchResponseDto> list, Pageable pageable) {

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        List<HotelSearchResponseDto> pageContent = list.subList(start, end);

        return new PageImpl<>(pageContent, pageable, list.size());
    }





}
