package com.aliens.hotel_reservation.models.entities;

import com.aliens.hotel_reservation.models.enums.RoomCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "room_types", uniqueConstraints = {@UniqueConstraint(columnNames = {"hotel_id", "name"})})
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id", nullable = false)
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    private RoomCategory name;

    private double basePrice;

    private short capacity;

    private short totalRooms;

}
