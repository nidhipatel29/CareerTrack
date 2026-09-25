package com.CareerTrack.service;

import java.util.List;
import org.springframework.data.domain.Page;

import com.CareerTrack.dto.JobCreateRequest;
import com.CareerTrack.dto.JobResponse;
import com.CareerTrack.dto.JobStatusUpdateRequest;
import com.CareerTrack.dto.JobUpdateRequest;
import com.CareerTrack.entity.Company;
import com.CareerTrack.entity.EmploymentType;

public interface JobService {

     JobResponse createJob(JobCreateRequest jobCreateRequest, String email);

     List<JobResponse> getJobs();

     JobResponse getJobById(Long id);

     JobResponse updateJob(Long id,JobUpdateRequest jobUpdateRequest, String email);

     void deleteJob(Long id, String email);

     List<JobResponse> searchJobsByTitle(String title);

     List<JobResponse> searchJobByLocation(String location);

     List<JobResponse> filterJobByEmployementType(EmploymentType employmentType);

     List<JobResponse> filterByCompanyId(Long id);

     List<JobResponse> filterJobs(
               String location,
               EmploymentType employmentType,
               Long companyId);

     Page<JobResponse> getJobsWithPagination(int page, int size, String sortBy, String direction);

     Page<JobResponse> filterJobsWithPagination(
               String title,
               String location,
               EmploymentType employmentType,
               Long companyId,
               int page,
               int size,
               String sortBy,
               String direction);

     JobResponse updateJobStatus(
               Long id,
               JobStatusUpdateRequest request,
               String email);

}
