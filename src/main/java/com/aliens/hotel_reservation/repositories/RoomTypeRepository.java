package com.aliens.hotel_reservation.repositories;

import com.aliens.hotel_reservation.models.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType,Long> {

    @Query("SELECT r.capacity FROM RoomType r WHERE r.id = :id")
    Short findCapacityById(long id);


    List<RoomType> findByHotelIdAndCapacity(long id, short guests);

    List<RoomType> findByHotelIdAndCapacityIsGreaterThanEqual(long id, short guests);
}
