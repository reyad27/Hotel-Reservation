package com.aliens.hotel_reservation.services.implementaions;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.mappers.RoomTypeMapper;
import com.aliens.hotel_reservation.mappers.SeasonalPriceMapper;
import com.aliens.hotel_reservation.models.dtos.RoomTypeRequestDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeResponseDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceRequestDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceResponseDto;
import com.aliens.hotel_reservation.models.entities.Hotel;
import com.aliens.hotel_reservation.models.entities.RoomType;
import com.aliens.hotel_reservation.models.entities.SeasonalPrice;
import com.aliens.hotel_reservation.repositories.HotelRepository;
import com.aliens.hotel_reservation.repositories.RoomTypeRepository;
import com.aliens.hotel_reservation.repositories.SeasonalPriceRepository;
import com.aliens.hotel_reservation.services.interfaces.RoomTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RomeTypeServiceImp implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeMapper mapper;
    private final SeasonalPriceMapper seasonalPriceMapper;
    private final SeasonalPriceRepository seasonalPriceRepository;


    @Override
    public RoomTypeResponseDto create(RoomTypeRequestDto dto) {
        Hotel hotel = hotelRepository.findById(dto.hotelId())
                .orElseThrow(() -> new HotelBusinessException("HotelId not found"));
        RoomType entity = mapper.toRoomTypeEntity(dto);
        entity.setHotel(hotel);
        RoomType saved = roomTypeRepository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public SeasonalPriceResponseDto addSeasonalPrice(SeasonalPriceRequestDto dto) {

        RoomType roomType = roomTypeRepository.findById(dto.roomTypeId())
                .orElseThrow(() -> new HotelBusinessException("RoomType not found"));

        checkIfThereIsOverlap(dto);

        SeasonalPrice seasonalPrice = seasonalPriceMapper.toSeasonalPriceEntity(dto);
        seasonalPrice.setRoomType(roomType);
        SeasonalPrice savedSeason = seasonalPriceRepository.save(seasonalPrice);
        return seasonalPriceMapper.toResponseDto(savedSeason);
    }

    public Page<RoomTypeResponseDto> getAllRoomTypes(Pageable pageable) {

        return roomTypeRepository.findAll(pageable)
                .map(entity -> {
                    // 1. Map the entity to the DTO using your existing mapper
                    RoomTypeResponseDto dto = mapper.toResponseDto(entity);

                    // 2. Perform the extra calculation and set the field
                    double seasonalFactor = applySeasonalPrice(dto.getId());
                    double price = dto.getBasePrice() * seasonalFactor;
                    dto.setPriceAfterApplySeasonalPrice(price);

                    return dto;
                });
    }



    @Override
    public List<SeasonalPriceResponseDto> getAllSeasonalPrices(Long roomTypeId) {
        roomTypeRepository.findById(roomTypeId)
                .orElseThrow(()->new HotelBusinessException(String.format("RoomTypeId %s not found", roomTypeId)));
        return seasonalPriceRepository.findAllByRoomTypeId(roomTypeId)
                .stream()
                .map(seasonalPriceMapper::toResponseDto)
                .toList();
    }

    private void checkIfThereIsOverlap(SeasonalPriceRequestDto dto) {
        List<SeasonalPrice> seasonalPrices = seasonalPriceRepository
                .findAllByRoomTypeIdAndToDateGreaterThanEqual(dto.roomTypeId(), LocalDate.now());

        for (SeasonalPrice seasonalPrice : seasonalPrices)
            if (seasonalPrice.getFromDate().isBefore(dto.toDate()) && !dto.fromDate().isAfter(seasonalPrice.getToDate()))
                throw new HotelBusinessException(
                        String.format("There is overlap with another seasonal price that starts with %s and ends with %s",
                                seasonalPrice.getFromDate(),
                                seasonalPrice.getToDate()));
    }

    private double applySeasonalPrice(Long roomTypeId) {
        SeasonalPrice seasonalPrice = seasonalPriceRepository
                .findByRoomTypeIdAndToDateGreaterThanEqual(roomTypeId, LocalDate.now()).orElse(null);
        if (seasonalPrice != null)
            return seasonalPrice.getMultiplier();
        return 1;
    }
}
