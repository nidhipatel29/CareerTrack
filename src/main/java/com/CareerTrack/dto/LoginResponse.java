package com.CareerTrack.dto;

import com.CareerTrack.entity.Role;

public class LoginResponse {

    private Long id;
    private String email;
    private Role role;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(Long id, String email, Role role,String token) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.token=token;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}