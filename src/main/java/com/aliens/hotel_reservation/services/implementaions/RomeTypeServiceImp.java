package com.aliens.hotel_reservation.services.implementaions;
import com.aliens.hotel_reservation.services.interfaces.RoomTypeService;
import com.aliens.hotel_reservation.models.dtos.RoomTypeRequestDto;
import com.aliens.hotel_reservation.models.dtos.RoomTypeResponseDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceRequestDto;
import com.aliens.hotel_reservation.models.dtos.SeasonalPriceResponseDto;
import com.aliens.hotel_reservation.models.entities.Hotel;
import com.aliens.hotel_reservation.models.entities.RoomType;
import com.aliens.hotel_reservation.models.entities.SeasonalPrice;
import com.aliens.hotel_reservation.mappers.RoomTypeMapper;
import com.aliens.hotel_reservation.mappers.SeasonalPriceMapper;
import com.aliens.hotel_reservation.repositories.HotelRepository;
import com.aliens.hotel_reservation.repositories.RoomTypeRepository;
import com.aliens.hotel_reservation.repositories.SeasonalPriceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RomeTypeServiceImp implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeMapper mapper;
    private final SeasonalPriceMapper seasonalPriceMapper;
    private final SeasonalPriceRepository seasonalPriceRepository;


//    public RomeTypeServiceImp(RoomTypeRepository roomTypeRepository , HotelRepository hotelRepository, RoomTypeMapper mapper)
//    {
//        this.roomTypeRepository=roomTypeRepository;
//        this.hotelRepository = hotelRepository;
//        this.mapper=mapper;
//    }

    public RoomTypeResponseDto create(RoomTypeRequestDto dto)
    {
        RoomType entity = mapper.toEntity(dto);
        Hotel hotel = hotelRepository.findById(dto.hotelId())
                .orElseThrow(() -> new RuntimeException("not found"));
        entity.setHotel(hotel);
        RoomType saved = roomTypeRepository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public SeasonalPriceResponseDto addSeasonalPrice(Long roomTypeId, SeasonalPriceRequestDto dto) {

        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("RoomType not found"));
        SeasonalPrice entity = seasonalPriceMapper.toEntity(dto);
        SeasonalPrice savedSeason = seasonalPriceRepository.save(entity);
        roomType.setSeasonalPrice(savedSeason);
        roomTypeRepository.save(roomType);
        return seasonalPriceMapper.toDto(savedSeason);
    }
    @Override
    @Transactional
    public RoomTypeResponseDto assignSeason(Long roomTypeId, Long seasonId) {

        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("RoomType not found"));

        SeasonalPrice seasonalPrice = seasonalPriceRepository.findById(seasonId)
                .orElseThrow(() -> new RuntimeException("Seasonal Price not found"));
        roomType.setSeasonalPrice(seasonalPrice);

        RoomType saved = roomTypeRepository.save(roomType);

        return mapper.toResponseDto(saved);
    }
    @Override
    public List<SeasonalPriceResponseDto> getAllSeasonalPrices() {
        return seasonalPriceRepository.findAll()
                .stream()
                .map(seasonalPriceMapper::toDto)
                .toList();
    }
}
