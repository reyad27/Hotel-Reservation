package com.aliens.hotel_reservation.services.implementaions;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.mappers.RoomMapper;
import com.aliens.hotel_reservation.models.dtos.RoomDto;
import com.aliens.hotel_reservation.models.entities.Room;
import com.aliens.hotel_reservation.models.entities.RoomType;
import com.aliens.hotel_reservation.models.enums.RoomStatus;
import com.aliens.hotel_reservation.repositories.RoomRepository;
import com.aliens.hotel_reservation.repositories.RoomTypeRepository;
import com.aliens.hotel_reservation.services.interfaces.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public RoomDto addRoom(RoomDto roomDto) {

        RoomType roomType = roomTypeRepository.findById(roomDto.roomTypeId())
                .orElseThrow(()->new HotelBusinessException(String.format("RoomTypeId %s not found", roomDto.roomTypeId())));

        roomType.setTotalRooms((short) (roomType.getTotalRooms()+1));
        Room room = roomMapper.toRoomEntity(roomDto);
        room.setRoomType(roomType);

        roomRepository.save(room);

        return roomMapper.toRoomDto(room);
    }

    @Override
    @Transactional
    public RoomDto changeRoomStatus(Long roomId, RoomStatus roomStatus) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()->new HotelBusinessException(String.format("RoomId %s not found", roomId)));

        room.setStatus(roomStatus);
        roomRepository.save(room);

        return roomMapper.toRoomDto(room);
    }
}
