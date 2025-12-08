package com.aliens.hotel_reservation.models.entities;

import com.aliens.hotel_reservation.models.entities.RoomType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "seasonal_prices")
public class SeasonalPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Column(name = "multiplier", nullable = false)
    private Double multiplier;

    @OneToMany(mappedBy = "seasonalPrice")
    private List<RoomType> roomTypes;
}
