package com.aliens.hotel_reservation.repositories;

import com.aliens.hotel_reservation.models.entities.SeasonalPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonalPriceRepository extends JpaRepository<SeasonalPrice,Long> {
}
