package com.CareerTrack.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ApplicationCreateRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    private LocalDate appliedDate;

    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;

    public ApplicationCreateRequest() {
    }

    public ApplicationCreateRequest(
            Long jobId,
            LocalDate appliedDate,
            String notes) {

        this.jobId = jobId;
        this.appliedDate = appliedDate;
        this.notes = notes;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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