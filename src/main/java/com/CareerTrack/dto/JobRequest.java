package com.CareerTrack.dto;

import java.math.BigDecimal;
import com.CareerTrack.entity.EmploymentType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class JobRequest {

    @NotBlank(message = "Job title is required")
    @Size(max = 150, message = "Job title must not exceed 150 characters")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Size(max = 150, message = "Location must not exceed 150 characters")
    private String location;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @DecimalMin(value = "0.0", inclusive = true, message = "Salary must not be negative")
    private BigDecimal salary;

    @NotNull(message = "Company id is required")
    private Long companyId;

    public JobRequest() {
    }

    public JobRequest(String title, String description, String location,
                       EmploymentType employmentType, BigDecimal salary,Long  companyId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.employmentType = employmentType;
        this.salary = salary;
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}