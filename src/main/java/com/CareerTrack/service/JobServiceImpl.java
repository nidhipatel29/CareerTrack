package com.CareerTrack.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.CareerTrack.dto.JobCreateRequest;
import com.CareerTrack.dto.JobResponse;
import com.CareerTrack.dto.JobStatusUpdateRequest;
import com.CareerTrack.dto.JobUpdateRequest;
import com.CareerTrack.entity.Company;
import com.CareerTrack.entity.CompanyStatus;
import com.CareerTrack.entity.EmploymentType;
import com.CareerTrack.entity.Job;
import com.CareerTrack.entity.JobStatus;
import com.CareerTrack.entity.Role;
import com.CareerTrack.entity.User;
import com.CareerTrack.exception.CompanyNotFoundException;
import com.CareerTrack.exception.InvalidRequestException;
import com.CareerTrack.exception.JobHasApplicationsException;
import com.CareerTrack.exception.JobNotFoundException;
import com.CareerTrack.exception.UserNotFoundException;
import com.CareerTrack.repository.ApplicationRepository;
import com.CareerTrack.repository.CompanyRepository;
import com.CareerTrack.repository.JobRepository;
import com.CareerTrack.repository.UserRepository;

@Service
public class JobServiceImpl implements JobService {

    private JobRepository jobRepository;
    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private ApplicationRepository applicationRepository;

    public JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository,
            UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    private void validateEmployerCanCreateJob(String email, Company company) {
        User currentUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (currentUser.getRole() != Role.EMPLOYER) {
            throw new AccessDeniedException("Only employers can create jobs");
        }

        if (company.getEmployer() == null || !company.getEmployer().getEmail().equalsIgnoreCase(email)) {
            throw new AccessDeniedException("Employer can create a job only for their own company");
        }
    }

    private void validateEmployerOwnsJob(String email, Job job, String action) {
        User currentUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (currentUser.getRole() != Role.EMPLOYER) {
            throw new AccessDeniedException("Only employers can " + action + " jobs");
        }

        if (job.getCompany() == null || job.getCompany().getEmployer() == null
                || !job.getCompany().getEmployer().getEmail().equalsIgnoreCase(email)) {
            throw new AccessDeniedException("Employer can " + action + " only their own job");
        }
    }

    private JobResponse mapToJobResponse(Job job) {

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getSalary(),
                job.getCreatedAt(),
                job.getCompany().getId(),
                job.getCompany().getName(),
                job.getStatus());
    }

    @Override
    public JobResponse createJob(JobCreateRequest jobCreateRequest, String email) {

        // resolve companyId → actual Company entity
        Company company = companyRepository.findById(jobCreateRequest.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(
                        "Company not found with id: " + jobCreateRequest.getCompanyId()));

        validateEmployerCanCreateJob(email, company);

        if (company.getStatus() == CompanyStatus.ARCHIVED) {
            throw new InvalidRequestException(
                    "Cannot create a job for an archived company");
        }

        // converting job request to job entity
        Job job = new Job();
        job.setTitle(jobCreateRequest.getTitle());
        job.setDescription(jobCreateRequest.getDescription());
        job.setLocation(jobCreateRequest.getLocation());
        job.setEmploymentType(jobCreateRequest.getEmploymentType());
        job.setSalary(jobCreateRequest.getSalary());
        company.addJob(job);

        // save job to db
        Job savedJob = jobRepository.save(job);

        // converting job entity-> job response
        return mapToJobResponse(savedJob);
    }

    @Override
    public List<JobResponse> getJobs() {
        List<Job> jobs = jobRepository.findAll();
        List<JobResponse> jobResponses = new ArrayList<>();
        // job->job response
        for (Job getJob : jobs) {
            JobResponse jobRespons = mapToJobResponse(getJob);
            jobResponses.add(jobRespons);

        }
        return jobResponses;
    }

    @Override
    public JobResponse getJobById(Long id) {
        Job retrivedJob = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException("job not found with id: " + id));

        return mapToJobResponse(retrivedJob);
    }

    @Override
    @Transactional
    public JobResponse updateJob(
            Long jobId,
            JobUpdateRequest jobUpdateRequest,
            String email) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(
                        "Job not found with id: " + jobId));

        validateEmployerOwnsJob(email, job, "update");

        job.setTitle(jobUpdateRequest.getTitle());
        job.setDescription(jobUpdateRequest.getDescription());
        job.setLocation(jobUpdateRequest.getLocation());
        job.setEmploymentType(jobUpdateRequest.getEmploymentType());
        job.setSalary(jobUpdateRequest.getSalary());

        return mapToJobResponse(job);
    }

    @Override
    public void deleteJob(Long id, String email) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));

        validateEmployerOwnsJob(email, job, "delete");

        if (applicationRepository.existsByJobId(id)) {
            throw new JobHasApplicationsException(
                    "Cannot delete job because applications already exist for it");
        }

        jobRepository.delete(job);

    }

    @Override
    public List<JobResponse> searchJobsByTitle(String title) {
        List<Job> jobs = jobRepository.findByTitleContainingIgnoreCase(title);
        return jobs.stream().map(this::mapToJobResponse).toList();
    }

    @Override
    public List<JobResponse> searchJobByLocation(String location) {
        return jobRepository.findByLocationIgnoreCase(location).stream().map(this::mapToJobResponse).toList();
    }

    @Override
    public List<JobResponse> filterJobByEmployementType(EmploymentType employmentType) {
        return jobRepository.findByEmploymentType(employmentType).stream().map(this::mapToJobResponse).toList();
    }

    @Override
    public List<JobResponse> filterByCompanyId(Long id) {
        companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(
                        "Company is not found: " + id));

        return jobRepository.findByCompanyId(id)
                .stream()
                .map(this::mapToJobResponse)
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
                .map(this::mapToJobResponse)
                .toList();
    }

    @Override
    public Page<JobResponse> getJobsWithPagination(int page, int size, String sortBy, String direction) {

        // set API validation for page and size
        if (page < 0 || size < 1 || size > 100) {
            throw new InvalidRequestException("wrong page or size value");
        }

        // set API validation for sorting (by field name)
        Set<String> allowedSortFields = Set.of("title", "salary", "createdAt", "location");
        if (!allowedSortFields.contains(sortBy)) {
            throw new InvalidRequestException(
                    "Invalid sort field: " + sortBy + ". Allowed fields are: title, salary, createdAt, location");
        }

        // set API validation for direction
        Sort.Direction sortDirection;
        if (direction.equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;

        } else if (direction.equalsIgnoreCase("asc")) {
            sortDirection = Sort.Direction.ASC;

        } else {
            // invalid direction
            throw new InvalidRequestException("Invalid direction:" + direction);
        }
        Sort sort = Sort.by(sortDirection, sortBy);
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Job> jobs = jobRepository.findAll(pageable);
        return jobs.map(this::mapToJobResponse);
    }

    @Override
    public Page<JobResponse> filterJobsWithPagination(
            String title,
            String location,
            EmploymentType employmentType,
            Long companyId,
            int page,
            int size,
            String sortBy,
            String direction) {

        // 1. Build specification

        Specification<Job> specification = Specification.where(
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        // 1.a>>>>>>>LOCATION FILTER
        if (location != null && !location.isBlank()) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("location")),
                            location.toLowerCase()));
        }

        // 1.b>>>>>>>>>>> EMPLOYMENT TYPE FILTER
        if (employmentType != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            root.get("employmentType"),
                            employmentType));
        }

        // 1.c>>>>>>>COMPANY FILTER
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

        // 1.d >>>>>>>>TITLE FILTER
        if (title != null && !title.isBlank()) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("title")),
                            "%" + title.toLowerCase() + "%"));
        }

        // 2. Validate page/size
        if (page < 0 || size < 1 || size > 100) {
            throw new InvalidRequestException("wrong page or size value");
        }

        // 3. Validate sortBy
        Set<String> allowedSortFields = Set.of("title", "salary", "createdAt", "location");
        if (!allowedSortFields.contains(sortBy)) {
            throw new InvalidRequestException(
                    "Invalid sort field: " + sortBy + ". Allowed fields are: title, salary, createdAt, location");
        }

        // 4. Validate direction
        Sort.Direction sortDirection;
        if (direction.equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;

        } else if (direction.equalsIgnoreCase("asc")) {
            sortDirection = Sort.Direction.ASC;

        } else {
            // invalid direction
            throw new InvalidRequestException("Invalid direction:" + direction);
        }

        // 5. Create Sort
        Sort sort = Sort.by(sortDirection, sortBy);

        // 6. Create PageRequest
        PageRequest pageable = PageRequest.of(page, size, sort);

        // 7. findAll(specification, pageable)

        // 8. map Page<Job> -> Page<JobResponse>

        return jobRepository.findAll(specification, pageable).map(this::mapToJobResponse);

    }

    @Override
    public JobResponse updateJobStatus(
            Long id,
            JobStatusUpdateRequest request,
            String email) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(
                        "Job not found with id: " + id));

        // Make sure this employer owns the job
        validateEmployerOwnsJob(email, job, "update status");

        if (request.getStatus() == JobStatus.OPEN
                && job.getCompany().getStatus() == CompanyStatus.ARCHIVED) {

            throw new InvalidRequestException(
                    "Cannot reopen a job for an archived company");
        }

        job.setStatus(request.getStatus());

        Job updatedJob = jobRepository.save(job);

        return mapToJobResponse(updatedJob);
    }
}
