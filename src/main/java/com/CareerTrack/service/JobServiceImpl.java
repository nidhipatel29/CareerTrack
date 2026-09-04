package com.CareerTrack.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.CareerTrack.dto.JobRequest;
import com.CareerTrack.dto.JobResponse;
import com.CareerTrack.entity.Company;
import com.CareerTrack.entity.Job;
import com.CareerTrack.exception.CompanyNotFoundException;
import com.CareerTrack.exception.JobNotFoundException;
import com.CareerTrack.repository.CompanyRepository;
import com.CareerTrack.repository.JobRepository;

@Service
public class JobServiceImpl implements JobService{

     private JobRepository jobRepository;
     private CompanyRepository companyRepository;


    public JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
    }

   private JobResponse maptoJobResponse(Job job){

    
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getSalary(),
                job.getCreatedAt(),
                job.getCompany().getId(),
                job.getCompany().getName());
   }

   @Override
    public JobResponse createJob(JobRequest request) {


        // resolve companyId → actual Company entity
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(
                        "Company not found with id: " + request.getCompanyId()));
        
        //converting job request to job entity
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setSalary(request.getSalary());
        job.setCompany(company);
       
        //save job to db
        Job savedJob = jobRepository.save(job);
        
        //converting job entity-> job response
        return maptoJobResponse(savedJob);
    }

   @Override
   public List<JobResponse> getJobs() {
    List<Job> jobs=jobRepository.findAll();
     List<JobResponse> jobResponses=new ArrayList<>();
    //job->job response
    for(Job getJob : jobs){
        JobResponse jobRespons=maptoJobResponse(getJob);
        jobResponses.add(jobRespons);

    }
       return jobResponses;
   }

   @Override
   public JobResponse getJobById(Long id) {
    Job retrivedJob=jobRepository.findById(id).
                                 orElseThrow(() -> new JobNotFoundException("job not found with id: " + id));


    return maptoJobResponse(retrivedJob);
   }

    }
    

