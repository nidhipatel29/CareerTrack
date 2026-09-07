package com.CareerTrack.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.CareerTrack.dto.RegisterRequest;
import com.CareerTrack.dto.RegisterResponse;
import com.CareerTrack.entity.Role;
import com.CareerTrack.entity.User;
import com.CareerTrack.repository.UserRepository;

@Service 

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository theUserRepository, PasswordEncoder thePasswordEncoder) {
        this.userRepository = theUserRepository;
        this.passwordEncoder = thePasswordEncoder;
    }

    private RegisterResponse mapToResponse(User user) {
        RegisterResponse registerResponse = new RegisterResponse(
                user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
        return registerResponse;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("user is already exist!");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(Role.JOB_SEEKER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }
}
