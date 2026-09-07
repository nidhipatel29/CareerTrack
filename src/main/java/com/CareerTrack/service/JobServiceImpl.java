package com.CareerTrack.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.CareerTrack.dto.JobRequest;
import com.CareerTrack.dto.JobResponse;
import com.CareerTrack.entity.Company;
import com.CareerTrack.entity.EmploymentType;
import com.CareerTrack.entity.Job;
import com.CareerTrack.exception.CompanyNotFoundException;
import com.CareerTrack.exception.JobNotFoundException;
import com.CareerTrack.repository.CompanyRepository;
import com.CareerTrack.repository.JobRepository;

@Service
public class JobServiceImpl implements JobService {

    private JobRepository jobRepository;
    private CompanyRepository companyRepository;

    public JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
    }

    private JobResponse maptoJobResponse(Job job) {

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

        // converting job request to job entity
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setSalary(request.getSalary());
        job.setCompany(company);

        // save job to db
        Job savedJob = jobRepository.save(job);

        // converting job entity-> job response
        return maptoJobResponse(savedJob);
    }

    @Override
    public List<JobResponse> getJobs() {
        List<Job> jobs = jobRepository.findAll();
        List<JobResponse> jobResponses = new ArrayList<>();
        // job->job response
        for (Job getJob : jobs) {
            JobResponse jobRespons = maptoJobResponse(getJob);
            jobResponses.add(jobRespons);

        }
        return jobResponses;
    }

    @Override
    public JobResponse getJobById(Long id) {
        Job retrivedJob = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException("job not found with id: " + id));

        return maptoJobResponse(retrivedJob);
    }

    @Override
    public JobResponse updateJob(Long jobId, JobRequest request) {

        // Step 1: does the Job exist?
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + jobId));

        // Step 2: does the Company (from the request) exist?
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(
                        "Company not found with id: " + request.getCompanyId()));

        // Step 3: both exist → update Job fields
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setSalary(request.getSalary());
        job.setCompany(company);

        // Step 4: save
        Job updatedJob = jobRepository.save(job);

        // Step 5: map to response
        return maptoJobResponse(updatedJob);
    }

    @Override
    public void deleteJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));

        jobRepository.delete(job);

    }

    @Override
    public List<JobResponse> searchJobsByTitle(String title) {
        List<Job> jobs = jobRepository.findByTitleContainingIgnoreCase(title);
        return jobs.stream().map(this::maptoJobResponse).toList();
    }

    @Override
    public List<JobResponse> searchJobByLocation(String location) {
        return jobRepository.findByLocationIgnoreCase(location).stream().map(this::maptoJobResponse).toList();
    }

    @Override
    public List<JobResponse> filterJobByEmployementType(EmploymentType employmentType) {
        return jobRepository.findByEmploymentType(employmentType).stream().map(this::maptoJobResponse).toList();
    }

    @Override
    public List<JobResponse> filterByCompanyId(Long id) {
        companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(
                        "Company is not found: " + id));

        return jobRepository.findByCompanyId(id)
                .stream()
                .map(this::maptoJobResponse)
                .toList();
    }

    @Override
    public List<JobResponse> filterJobs(
            String location,
            EmploymentType employmentType,
            Long companyId) {

        // Start with an always-true condition
        Specification<Job> specification = Specification.where(
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        // LOCATION FILTER
        if (location != null && !location.isBlank()) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("location")),
                            location.toLowerCase()));
        }

        // EMPLOYMENT TYPE FILTER
        if (employmentType != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            root.get("employmentType"),
                            employmentType));
        }

        // COMPANY FILTER
        if (companyId != null) {

            // Verify that the company actually exists
            companyRepository.findById(companyId)
                    .orElseThrow(() -> new CompanyNotFoundException(
                            "Company is not found: " + companyId));

            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            root.get("company").get("id"),
                            companyId));
        }

        return jobRepository.findAll(specification)
                .stream()
                .map(this::maptoJobResponse)
                .toList();
    }

    @Override
    public Page<JobResponse> getJobsWithPagination(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection;

        if (direction.equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        } else {
            sortDirection = Sort.Direction.ASC;
        }
        Sort sort = Sort.by(sortDirection, sortBy);
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Job> jobs = jobRepository.findAll(pageable);
        return jobs.map(this::maptoJobResponse);
    }
}
