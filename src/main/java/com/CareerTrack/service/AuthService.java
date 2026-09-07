package com.CareerTrack.service;

import com.CareerTrack.dto.RegisterRequest;
import com.CareerTrack.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);
}
