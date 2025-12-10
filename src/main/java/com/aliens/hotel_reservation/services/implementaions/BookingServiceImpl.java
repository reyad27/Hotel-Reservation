package com.aliens.hotel_reservation.services.implementaions;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.mappers.BookingMapper;
import com.aliens.hotel_reservation.models.dtos.BookingDto;
import com.aliens.hotel_reservation.models.dtos.ResponseBookingDto;
import com.aliens.hotel_reservation.models.entities.*;
import com.aliens.hotel_reservation.models.enums.BookingStatus;
import com.aliens.hotel_reservation.models.enums.RoomStatus;
import com.aliens.hotel_reservation.repositories.*;
import com.aliens.hotel_reservation.services.interfaces.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final SeasonalPriceRepository seasonalPriceRepository;
    private final BookingMapper bookingMapper;


    @Override
    @Transactional
    public ResponseBookingDto createBooking(BookingDto bookingDto) {

        Booking booking = assembleBookingFromDto(bookingDto);
        checkCapacity(bookingDto, booking.getRoomType());
        double totalPice = getTotalPiceAndApplySeasonalPriceIfExist(bookingDto, booking.getRoomType());

        booking.setTotalPrice(totalPice);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setFakePaymentTransactionId(UUID.randomUUID().toString());

        bookingRepository.save(booking);

        return bookingMapper.fromBookingToResponse(booking);
    }

    @Override
    public Page<ResponseBookingDto> getAllBookingsByGuestId(Long guestId, Pageable pageable) {
        guestRepository.findById(guestId)
                .orElseThrow(()->new HotelBusinessException("Your provided guestId is not known for me"));

        return bookingRepository.findAllByGuestId(guestId,pageable)
                .map(bookingMapper::fromBookingToResponse);
    }

    @Override
    public Page<ResponseBookingDto> getAllBookingsByHotelId(Long hotelId, Pageable pageable) {
        hotelRepository.findById(hotelId)
                .orElseThrow(()->new HotelBusinessException("Your provided hotelId is not known for me"));

        return bookingRepository.findAllByHotelId(hotelId, pageable)
                .map(bookingMapper::fromBookingToResponse);
    }

    @Override
    public ResponseBookingDto cancelBookingByGuest(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new HotelBusinessException("your provided bookingId is not known for me"));

        if(LocalDate.now().isBefore(booking.getStartDate()))
            booking.setStatus(BookingStatus.CANCELLED);
        else
            throw new HotelBusinessException("Cancellation not allowed after check-in");

        bookingRepository.save(booking);

        return bookingMapper.fromBookingToResponse(booking);
    }

    @Override
    public ResponseBookingDto cancelBookingByManager(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new HotelBusinessException("your provided bookingId is not known for me"));
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return bookingMapper.fromBookingToResponse(booking);
    }

    private Booking assembleBookingFromDto(BookingDto bookingDto) {

        Guest guest = guestRepository.findById(bookingDto.guestId())
                .orElseThrow(() -> new HotelBusinessException("GuestId not found"));
        Hotel hotel = hotelRepository.findById(bookingDto.hotelId())
                .orElseThrow(() -> new HotelBusinessException("HotelId not found"));
        RoomType roomType = roomTypeRepository.findById(bookingDto.roomTypeId())
                .orElseThrow(() -> new HotelBusinessException("RoomTypeId not found"));
        Room room = findAvailableRoom(roomType.getId(), bookingDto.startDate(), bookingDto.endDate())
                .orElseThrow(() -> new HotelBusinessException("No room available with " + roomType.getName()));

        Booking booking = new Booking();
        booking.setGuest(guest);
        booking.setHotel(hotel);
        booking.setRoomType(roomType);
        booking.setRoom(room);
        booking.setStartDate(bookingDto.startDate());
        booking.setEndDate(bookingDto.endDate());

        return booking;
    }

    private double getTotalPiceAndApplySeasonalPriceIfExist(BookingDto bookingDto, RoomType roomType) {

        long diffInDays = ChronoUnit.DAYS.between(bookingDto.startDate(), bookingDto.endDate());
        double totalPice = diffInDays * roomType.getBasePrice();

        List<SeasonalPrice> seasonalPrices = seasonalPriceRepository
                .findAllByRoomTypeIdAndToDateGreaterThanEqual(roomType.getId(),LocalDate.now());

        for(SeasonalPrice seasonalPrice: seasonalPrices)
            if (!bookingDto.startDate().isBefore(seasonalPrice.getFromDate()) && !bookingDto.endDate().isAfter(seasonalPrice.getToDate()))
                totalPice *= seasonalPrice.getMultiplier();

        return totalPice;
    }

    private void checkCapacity(BookingDto bookingDto, RoomType roomType) {
        if (bookingDto.guests() > roomTypeRepository.findCapacityById(roomType.getId()))
            throw new HotelBusinessException("The number of guests in over than capacity, capacity is: " + roomType.getCapacity());
    }

    private Optional<Room> findAvailableRoom(Long roomTypeId, LocalDate startDate, LocalDate endDate){

        List<Room> rooms = roomRepository.findAllByRoomTypeIdAndStatus(roomTypeId, RoomStatus.ACTIVE);
        List<Booking> bookings = bookingRepository.findAllByRoomTypeIdAndEndDateGreaterThanEqual(roomTypeId,LocalDate.now());

        for(Room room: rooms){

            boolean available = true;
            List<Booking> bookingsFilteredBasedOnRoomId = bookings.stream()
                    .filter(booking -> booking.getRoom().getId() == room.getId()).toList();

            for(Booking booking: bookingsFilteredBasedOnRoomId)
                if (booking.getStartDate().isBefore(endDate) && !startDate.isAfter(booking.getEndDate())) {
                    available = false;
                    break;
                }

            if(available)
                return Optional.of(room);
        }

        return Optional.empty();
    }

}
