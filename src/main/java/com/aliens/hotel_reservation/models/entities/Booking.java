package com.aliens.hotel_reservation.models.entities;

import com.aliens.hotel_reservation.models.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "bookings", uniqueConstraints = {@UniqueConstraint(columnNames = {"room_id", "start_date", "end_date"})})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "guest_id", referencedColumnName = "id")
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_type_id", referencedColumnName = "id")
    private RoomType roomType;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    private LocalDate startDate;

    private LocalDate endDate;

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private String fakePaymentTransactionId;
}
