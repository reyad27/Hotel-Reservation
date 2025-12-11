package com.aliens.hotel_reservation.controllers;

import com.aliens.hotel_reservation.models.dtos.RoomDto;
import com.aliens.hotel_reservation.models.enums.RoomStatus;
import com.aliens.hotel_reservation.services.interfaces.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> addRoom(@Valid @RequestBody RoomDto roomDto){
        RoomDto roomResponseDto = roomService.addRoom(roomDto);
        return new ResponseEntity<>(roomResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<RoomDto> changeRoomStatus(@PathVariable Long roomId, @RequestParam RoomStatus roomStatus){
        RoomDto roomResponseDto = roomService.changeRoomStatus(roomId, roomStatus);
        return new ResponseEntity<>(roomResponseDto, HttpStatus.OK);
    }
}
