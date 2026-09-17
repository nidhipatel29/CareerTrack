package com.CareerTrack.service;

import java.util.List;
import com.CareerTrack.dto.ApplicationCreateRequest;
import com.CareerTrack.dto.ApplicationUpdateRequest;

import com.CareerTrack.dto.ApplicationResponse;
import com.CareerTrack.dto.ApplicationStatusUpdateRequest;

public interface ApplicationService {

    // CREATE
    ApplicationResponse createApplication(ApplicationCreateRequest applicationCreateRequest,String email);

    // GET ALL
    List<ApplicationResponse> getMyApplications(String email);

    // GET BY ID
    ApplicationResponse getApplicationById(Long id,String email);

    // UPDATE
    ApplicationResponse updateApplication(
            Long id,
            ApplicationUpdateRequest applicationUpdateRequest,String email);

    // DELETE
    void deleteApplication(Long id,String email);

    //Get Application By UserId
    List<ApplicationResponse> getApplicationsByUserId(Long userId);

    //Get applications by jobId
    List<ApplicationResponse> getApplicationByJobId(Long jobId,String email);

    ApplicationResponse updateApplicationStatus(
        Long applicationId,
        ApplicationStatusUpdateRequest request,
        String email);

}