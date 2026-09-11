package com.CareerTrack.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CareerTrack.dto.LoginRequest;
import com.CareerTrack.dto.LoginResponse;
import com.CareerTrack.dto.RegisterRequest;
import com.CareerTrack.dto.RegisterResponse;
import com.CareerTrack.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

@PostMapping("/register")
public RegisterResponse register(@Valid @RequestBody RegisterRequest theRegisterRequest){
    return  authService.register(theRegisterRequest);
}

@PostMapping("/login")
public ResponseEntity<LoginResponse> login( @Valid @RequestBody LoginRequest request) {

    LoginResponse response = authService.login(request);

    return ResponseEntity.ok(response);
}
    
    
}
