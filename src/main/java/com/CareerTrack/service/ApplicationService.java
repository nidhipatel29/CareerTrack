package com.CareerTrack.service;

import java.util.List;

import com.CareerTrack.dto.ApplicationRequest;
import com.CareerTrack.dto.ApplicationResponse;

public interface ApplicationService {

    // CREATE
    ApplicationResponse createApplication(ApplicationRequest applicationRequest);

    // GET ALL
    List<ApplicationResponse> getAllApplications();

    // GET BY ID
    ApplicationResponse getApplicationById(Long id);

    // UPDATE
    ApplicationResponse updateApplication(
            Long id,
            ApplicationRequest applicationRequest);

    // DELETE
    void deleteApplication(Long id);
}