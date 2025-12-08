package com.aliens.hotel_reservation.repositories;

import com.aliens.hotel_reservation.models.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findByCityIgnoreCase(String city);//to ignore the case of the cities

}
