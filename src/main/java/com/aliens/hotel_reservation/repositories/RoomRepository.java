package com.aliens.hotel_reservation.repositories;

import com.aliens.hotel_reservation.models.entities.Room;
import com.aliens.hotel_reservation.models.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByRoomTypeIdAndStatus(Long roomTypeId, RoomStatus roomStatus);
}