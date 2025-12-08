package com.aliens.hotel_reservation.repositories;

import com.aliens.hotel_reservation.models.entities.RoomType;
import com.aliens.hotel_reservation.models.entities.SeasonalPrice;
import jakarta.validation.constraints.Min;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType,Long> {

    @Query("SELECT r.capacity FROM RoomType r WHERE r.id = :id")
    Short findCapacityById(long id);


    List<RoomType> findByHotelIdAndCapacity(long id, short guests);
}
