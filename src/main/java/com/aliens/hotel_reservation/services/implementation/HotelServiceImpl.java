package com.aliens.hotel_reservation.services.implementation;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.mappers.RoomTypesMapper;
import com.aliens.hotel_reservation.mappers.HotelMapper;
import com.aliens.hotel_reservation.models.dtos.HotelRequestDto;
import com.aliens.hotel_reservation.models.dtos.HotelSearchResponseDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeDto;
import com.aliens.hotel_reservation.models.entities.Hotel;
import com.aliens.hotel_reservation.models.entities.RoomType;
import com.aliens.hotel_reservation.models.entities.SeasonalPrice;
import com.aliens.hotel_reservation.repositories.*;
import com.aliens.hotel_reservation.services.HotelService;
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
    public Page<HotelSearchResponseDto> searchHotel(String city, LocalDate from, LocalDate to, int guest, Pageable pageable) {
        if (from.isAfter(to)) {
            throw new HotelBusinessException("Check-in date cannot be after check-out date");
        }

        List<Hotel> hotels = hotelRepository.findByCityIgnoreCase(city);

        if (hotels.isEmpty()) {
            throw new HotelBusinessException("No hotels found in city: " + city);
        }

        List<HotelSearchResponseDto> hotelResponseList = new ArrayList<>();

        for (Hotel hotel : hotels) {

            List<RoomType> roomTypes = roomTypeRepository.findByHotelIdAndCapacity(hotel.getId(), guest);

            if (roomTypes.isEmpty()) {
                throw new HotelBusinessException("No room types available for " + guest + " guests in hotel: " + hotel.getName());
            }

            List<RoomTypeDto> roomTypeDtos = new ArrayList<>();

            for (RoomType roomType : roomTypes) {

                int activeRoom = roomRepository.countActiveRooms(roomType.getId());
                int conflictRoom = bookingRepository.countConflictingBookings(roomType.getId(), from, to);
                int availableRooms = activeRoom - conflictRoom;

                if (availableRooms <= 0) continue;

                double totalPrice = calculateTotalPrice(roomType, from, to);
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
        Hotel hotel=hotelMapper.hotelRequestDtoToHotel(hotelDto);
        Hotel savedHotel=hotelRepository.save(hotel);
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

    public double calculateTotalPrice(RoomType roomType,LocalDate from,LocalDate to){
        long numberOfDays = ChronoUnit.DAYS.between(from, to);
        if (numberOfDays <= 0) return 0;

        double totalPrice = 0;

        for (int i = 0; i < numberOfDays; i++) {
            LocalDate currentDate = from.plusDays(i);
            double multiplier = getSeasonalMultiplier(roomType.getId(), currentDate);
            totalPrice += roomType.getBasePrice() * multiplier;
        }

        return totalPrice;

    }
    //return seasonal price  multiplier
    public double getSeasonalMultiplier(Long roomTypeId,LocalDate date){

        List<SeasonalPrice> seasonalPrices =
                seasonalPriceRepository
                        .findByRoomTypeIdAndFromDateLessThanEqualAndEndDateGreaterThanEqual(
                                roomTypeId, date,  date
                        );
        return seasonalPrices.stream()
                .mapToDouble(SeasonalPrice::getMultiplier)//mapping the multiplier [1.5,1.2,1.3]
                .max()//take the highest multiplier from the multiplier
                .orElse(1.0);





    }


}
