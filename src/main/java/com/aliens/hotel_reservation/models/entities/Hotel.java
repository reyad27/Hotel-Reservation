package com.aliens.hotel_reservation.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hotels", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "city", "address"})})
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany
    private RoomType roomType;

    private String name;

    private String city;

    private String address;

}
