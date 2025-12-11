package com.aliens.hotel_reservation.services.implementaions;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.mappers.HotelManagerMapper;
import com.aliens.hotel_reservation.models.dtos.HotelManagerCreationDto;
import com.aliens.hotel_reservation.models.dtos.HotelManagerDto;
import com.aliens.hotel_reservation.models.entities.Hotel;
import com.aliens.hotel_reservation.models.entities.HotelManager;
import com.aliens.hotel_reservation.security.entity.User;
import com.aliens.hotel_reservation.repositories.HotelManagerRepository;
import com.aliens.hotel_reservation.repositories.HotelRepository;
import com.aliens.hotel_reservation.security.enums.Role;
import com.aliens.hotel_reservation.security.repository.UserRepository;
import com.aliens.hotel_reservation.services.interfaces.HotelManagerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class HotelManagerServiceImpl implements HotelManagerService {

    private final HotelManagerRepository hotelManagerRepository;
    private final HotelManagerMapper mapper;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public HotelManagerDto getManagerById(long id) {
        return hotelManagerRepository.findById(id)
                .map(mapper::hotelManagerToHotelManagerDto)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
    }
    @Override
    @Transactional
    public HotelManagerDto addNewManager(HotelManagerCreationDto dto) {

        User user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.MANAGER)
                .enabled(true)
                .build();

        userRepository.save(user);

        Hotel hotel = hotelRepository.findById(dto.hotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        HotelManager hotelManager = HotelManager.builder()
                .user(user)
                .hotel(hotel)
                .build();

        HotelManager saved = hotelManagerRepository.save(hotelManager);
        return mapper.hotelManagerToHotelManagerDto(saved);
    }

    @Override
    public void deleteManagerById(long id) throws HotelBusinessException {
        hotelManagerRepository.deleteById(id);
    }

    @Override
    public Page<HotelManagerDto> getAllManagers(Pageable pageable) {
        return hotelManagerRepository.findAll(pageable)
                .map(mapper::hotelManagerToHotelManagerDto);
    }


}
