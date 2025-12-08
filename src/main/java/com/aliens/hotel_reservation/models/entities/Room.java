package com.aliens.hotel_reservation.models.entities;

import com.aliens.hotel_reservation.models.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_type_id", referencedColumnName = "id")
    private RoomType roomType;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;
}
