package com.aliens.hotel_reservation.repositories;

import com.aliens.hotel_reservation.models.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
                SELECT COUNT(b) > 0
                FROM Booking b
                WHERE b.room.id = :roomId
                  AND b.startDate < :newEndDate
                  AND :newStartDate < b.endDate
            """)
    Boolean checkIfThereIsConflict(
            @Param("roomId") Long roomTypeId,
            @Param("newStartDate") LocalDate newStartDate,
            @Param("newEndDate") LocalDate newEndDate
    );

    List<Booking> findAllByRoomTypeIdAndEndDateGreaterThanEqual(Long roomTypeId, LocalDate now);

    Page<Booking> findAllByGuestId(Long guestId, Pageable pageable);

    Page<Booking> findAllByHotelId(Long hotelId, Pageable pageable);
}
