package com.aliens.hotel_reservation.models.entities;

import com.aliens.hotel_reservation.models.enums.RoomCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "room_types")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "season_id", referencedColumnName = "id")
    private SeasonalPrice seasonalPrice;

    private RoomCategory name;

    private double basePrice;

    private short capacity;

    private short totalRooms;

}
