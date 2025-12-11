package com.aliens.hotel_reservation.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hotels", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "city", "address"})})
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "hotel")
    private List<RoomType> roomTypes;

    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

}
