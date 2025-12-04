package com.aliens.hotel_reservation.repositories;

import com.aliens.hotel_reservation.models.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType,Long> {
}
