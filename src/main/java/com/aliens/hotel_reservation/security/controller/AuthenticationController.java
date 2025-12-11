package com.aliens.hotel_reservation.security.controller;

import com.aliens.hotel_reservation.security.dto.AuthenticationRequest;
import com.aliens.hotel_reservation.security.dto.AuthenticationResponse;
import com.aliens.hotel_reservation.security.dto.RegisterRequest;
import com.aliens.hotel_reservation.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}