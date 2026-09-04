package com.CareerTrack.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.CareerTrack.entity.ApplicationStatus;

public class ApplicationResponse {

    private Long id;

    private Long userId;
    private String userName;

    private Long jobId;
    private String jobTitle;

    private Long companyId;
    private String companyName;

    private ApplicationStatus status;

    private LocalDate appliedDate;

    private String notes;

    private LocalDateTime createdAt;

    public ApplicationResponse() {
    }

    public ApplicationResponse(
            Long id,
            Long userId,
            String userName,
            Long jobId,
            String jobTitle,
            Long companyId,
            String companyName,
            ApplicationStatus status,
            LocalDate appliedDate,
            String notes,
            LocalDateTime createdAt) {

        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyId = companyId;
        this.companyName = companyName;
        this.status = status;
        this.appliedDate = appliedDate;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}