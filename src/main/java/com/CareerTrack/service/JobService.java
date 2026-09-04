package com.CareerTrack.service;

import java.util.List;

import com.CareerTrack.dto.JobRequest;
import com.CareerTrack.dto.JobResponse;
import com.CareerTrack.entity.Company;

public interface JobService {

     JobResponse createJob(JobRequest request);

     List<JobResponse> getJobs();

     JobResponse getJobById(Long id);
    
} 
