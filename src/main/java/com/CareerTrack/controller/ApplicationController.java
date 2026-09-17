package com.CareerTrack.controller;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CareerTrack.dto.ApplicationCreateRequest;
import com.CareerTrack.dto.ApplicationResponse;
import com.CareerTrack.dto.ApplicationStatusUpdateRequest;
import com.CareerTrack.dto.ApplicationUpdateRequest;
import com.CareerTrack.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService theApplicationService) {
        this.applicationService = theApplicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(
            @Valid @RequestBody ApplicationCreateRequest applicationCreateRequest, Authentication authentication) {
        String email = authentication.getName();
        ApplicationResponse applicationResponse = applicationService.createApplication(applicationCreateRequest, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationResponse);
    }

    @GetMapping
    public List<ApplicationResponse> getMyApplications(Authentication authentication) {

        String email = authentication.getName();
        return applicationService.getMyApplications(email);
    }

    @GetMapping("/{id}")
    public ApplicationResponse getApplicationById(@PathVariable Long id, Authentication authentication) {

        String email = authentication.getName();
        return applicationService.getApplicationById(id, email);
    }

    @GetMapping("/job/{jobId}")

    public List<ApplicationResponse> getApplicationByJobId(@PathVariable Long jobId, Authentication authentication) {
        String email = authentication.getName();
        return applicationService.getApplicationByJobId(jobId, email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponse> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationUpdateRequest applicationUpdateRequest, Authentication authentication) {
        String email = authentication.getName();
        ApplicationResponse applicationResponse = applicationService.updateApplication(id, applicationUpdateRequest, email);
        return ResponseEntity.ok(applicationResponse);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        ApplicationResponse response = applicationService.updateApplicationStatus(
                id,
                request,
                email);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        applicationService.deleteApplication(id, email);
        return ResponseEntity.noContent().build();
    }
}
