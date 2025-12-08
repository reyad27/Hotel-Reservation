package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.models.dtos.HotelManagerCreationDto;
import com.aliens.hotel_reservation.models.dtos.HotelManagerDto;
import com.aliens.hotel_reservation.services.implementaions.HotelManagerServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/managers")
public class ManagerController {

    private final HotelManagerServiceImpl hotelManagerService;

    @GetMapping("/{id}")
    public HotelManagerDto getManagerById(@PathVariable long id) throws HotelBusinessException {
        return hotelManagerService.getManagerById(id);
    }


    @PostMapping
    public HotelManagerDto addNewManager(@RequestBody HotelManagerCreationDto dto) {
        return hotelManagerService.addNewManager(dto);
    }


    @DeleteMapping("/{id}")
    public void deleteManager(@PathVariable long id) {
        hotelManagerService.deleteManagerById(id);
    }


}
