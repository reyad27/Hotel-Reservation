package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.exceptions.HotelBusinessException;
import com.aliens.hotel_reservation.models.dtos.HotelManagerCreationDto;
import com.aliens.hotel_reservation.models.dtos.HotelManagerDto;
import com.aliens.hotel_reservation.services.implementaions.HotelManagerServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/managers")
public class ManagerController {

    private final HotelManagerServiceImpl hotelManagerService;

    @GetMapping("/{id}")
    public ResponseEntity<HotelManagerDto> getManagerById(@PathVariable long id) throws HotelBusinessException {
        HotelManagerDto hotelManagerDto = hotelManagerService.getManagerById(id);
        return new ResponseEntity<>(hotelManagerDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<HotelManagerDto>> getAllManagers(Pageable pageable){
        Page<HotelManagerDto> hotelManagersDto = hotelManagerService.getAllManagers(pageable);
        return new ResponseEntity<>(hotelManagersDto, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<HotelManagerDto> addNewManager(@RequestBody HotelManagerCreationDto dto) {
        HotelManagerDto hotelManagerDto = hotelManagerService.addNewManager(dto);
        return new ResponseEntity<>(hotelManagerDto, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable long id) {
        hotelManagerService.deleteManagerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
