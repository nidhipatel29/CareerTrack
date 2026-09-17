package com.CareerTrack.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

public class ApplicationUpdateRequest {

    private LocalDate appliedDate;

    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;

    public ApplicationUpdateRequest() {
    }

    public ApplicationUpdateRequest(
            LocalDate appliedDate,
            String notes) {

        this.appliedDate = appliedDate;
        this.notes = notes;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}