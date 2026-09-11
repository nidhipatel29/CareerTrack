package com.CareerTrack.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.CareerTrack.dto.LoginRequest;
import com.CareerTrack.dto.LoginResponse;
import com.CareerTrack.dto.RegisterRequest;
import com.CareerTrack.dto.RegisterResponse;
import com.CareerTrack.entity.Role;
import com.CareerTrack.entity.User;
import com.CareerTrack.exception.EmailAlreadyExistsException;
import com.CareerTrack.repository.UserRepository;

@Service

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthServiceImpl(UserRepository theUserRepository, PasswordEncoder thePasswordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService,
            CustomUserDetailsService customUserDetailsService) {
        this.userRepository = theUserRepository;
        this.passwordEncoder = thePasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;

    }

    private RegisterResponse mapToResponse(User user) {
        RegisterResponse registerResponse = new RegisterResponse(
                user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
        return registerResponse;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("user is already exist!");
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

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));

        User user=userRepository.findByEmailIgnoreCase(request.getEmail()).orElseThrow();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                token);
    }
}
