package com.aliens.hotel_reservation.models.entities;

import com.aliens.hotel_reservation.models.enums.RoomStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rooms", uniqueConstraints = {@UniqueConstraint(columnNames = {"room_type_id", "roomNumber"})})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_type_id", referencedColumnName = "id", nullable = false)
    private RoomType roomType;

    @NotBlank
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;
}
