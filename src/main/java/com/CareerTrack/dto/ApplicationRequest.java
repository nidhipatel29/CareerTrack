package com.CareerTrack.dto;

import java.time.LocalDate;

import com.CareerTrack.entity.ApplicationStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ApplicationRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Job id is required")
    private Long jobId;

    @NotNull(message = "Application status is required")
    private ApplicationStatus status;

    private LocalDate appliedDate;

    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    private String notes;

    public ApplicationRequest() {
    }

    public ApplicationRequest(
            Long userId,
            Long jobId,
            ApplicationStatus status,
            LocalDate appliedDate,
            String notes) {

        this.userId = userId;
        this.jobId = jobId;
        this.status = status;
        this.appliedDate = appliedDate;
        this.notes=notes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
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