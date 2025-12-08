package com.aliens.hotel_reservation.models.entities;

import com.aliens.hotel_reservation.models.entities.Hotel;
import com.aliens.hotel_reservation.models.enums.RoomCategory;
import com.aliens.hotel_reservation.models.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Entity
@Getter
@Setter
@Table(name = "room_type")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;
    @Enumerated(EnumType.STRING)
    private RoomStatus name;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private RoomCategory category;
    @ManyToOne
    @JoinColumn(name = "season_id", referencedColumnName = "id")
    private SeasonalPrice seasonalPrice;
    private double basePrice;
    private short capacity;
    private short totalRooms;
}
