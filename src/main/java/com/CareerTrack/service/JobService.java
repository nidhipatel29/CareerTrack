package com.CareerTrack.service;

import java.util.List;
import org.springframework.data.domain.Page;
import com.CareerTrack.dto.JobRequest;
import com.CareerTrack.dto.JobResponse;
import com.CareerTrack.entity.Company;
import com.CareerTrack.entity.EmploymentType;

public interface JobService {

     JobResponse createJob(JobRequest request);

     List<JobResponse> getJobs();

     JobResponse getJobById(Long id);

     JobResponse updateJob(Long id, JobRequest request);

     void deleteJob(Long id);

     List<JobResponse> searchJobsByTitle(String title);

     List<JobResponse> searchJobByLocation(String location);

     List<JobResponse> filterJobByEmployementType(EmploymentType employmentType);

     List<JobResponse> filterByCompanyId(Long id);

     List<JobResponse> filterJobs(
        String location,
        EmploymentType employmentType,
        Long companyId);

     Page<JobResponse> getJobsWithPagination(int page, int size, String sortBy, String direction);



    
} 
