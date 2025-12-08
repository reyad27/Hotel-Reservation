package com.aliens.hotel_reservation.services.implementaions;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.mappers.HotelManagerMapper;
import com.aliens.hotel_reservation.models.dtos.HotelManagerCreationDto;
import com.aliens.hotel_reservation.models.dtos.HotelManagerDto;
import com.aliens.hotel_reservation.models.entities.Hotel;
import com.aliens.hotel_reservation.models.entities.HotelManager;
import com.aliens.hotel_reservation.models.entities.User;
import com.aliens.hotel_reservation.repositories.HotelManagerRepository;
import com.aliens.hotel_reservation.repositories.HotelRepository;
import com.aliens.hotel_reservation.repositories.UserRepository;
import com.aliens.hotel_reservation.services.interfaces.HotelManagerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class HotelManagerServiceImpl implements HotelManagerService {
    private final HotelManagerRepository hotelManagerRepository;
    private final HotelManagerMapper mapper;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    @Override
    public HotelManagerDto getManagerById(long id) {
        return hotelManagerRepository.findById(id)
                .map(mapper::hotelManagerToHotelManagerDto)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
    }
    @Override
    public HotelManagerDto addNewManager(HotelManagerCreationDto dto) {

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Hotel hotel = hotelRepository.findById(dto.hotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        HotelManager manager = new HotelManager();
        manager.setUser(user);
        manager.setHotel(hotel);

        HotelManager saved = hotelManagerRepository.save(manager);

        return mapper.hotelManagerToHotelManagerDto(saved);
    }

    @Override
    public void deleteManagerById(long id) throws HotelBusinessException {
        hotelManagerRepository.deleteById(id);
    }


}
