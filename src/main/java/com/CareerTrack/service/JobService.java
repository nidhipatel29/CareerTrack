package com.CareerTrack.service;

import com.CareerTrack.dto.JobRequest;
import com.CareerTrack.dto.JobResponse;

public interface JobService {

     JobResponse createJob(JobRequest request);
    
} 
