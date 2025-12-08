package com.aliens.hotel_reservation.exceptions;

public class HotelBusinessException extends RuntimeException{
    public HotelBusinessException(String message){
        super(message);
    }
}
