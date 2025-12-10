package com.aliens.hotel_reservation.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "seasonal_prices", uniqueConstraints = {@UniqueConstraint(columnNames = {"room_type_id", "from_date", "to_date"})})
public class SeasonalPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_type_id", referencedColumnName = "id")
    private RoomType roomType;

    private LocalDate fromDate;

    private LocalDate toDate;

    private double multiplier;

}
