package com.aliens.hotel_reservation.repositories;

import com.aliens.hotel_reservation.models.entities.SeasonalPrice;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonalPriceRepository extends JpaRepository<SeasonalPrice,Long> {

    List<SeasonalPrice> findAllByRoomTypeIdAndToDateGreaterThanEqual(Long roomTypeId, LocalDate now);

    Optional<SeasonalPrice> findByRoomTypeIdAndToDateGreaterThanEqual(Long roomTypeId, LocalDate now);

    List<SeasonalPrice> findAllByRoomTypeId(Long roomTypeId);
}
