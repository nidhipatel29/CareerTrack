package com.CareerTrack.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.CareerTrack.entity.Application;
import com.CareerTrack.entity.Company;
import com.CareerTrack.dto.ApplicationRequest;
import com.CareerTrack.dto.ApplicationResponse;
import com.CareerTrack.entity.Job;
import com.CareerTrack.entity.User;
import com.CareerTrack.exception.ApplicationNotFoundException;
import com.CareerTrack.exception.DuplicateApplicationException;
import com.CareerTrack.exception.JobNotFoundException;
import com.CareerTrack.exception.UserNotFoundException;
import com.CareerTrack.repository.ApplicationRepository;
import com.CareerTrack.repository.JobRepository;
import com.CareerTrack.repository.UserRepository;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private ApplicationRepository applicationRepository;
    private UserRepository userRepository;
    private JobRepository jobRepository;

    public ApplicationServiceImpl(ApplicationRepository theApplicationRepository, UserRepository theUserRepository,
            JobRepository theJobRepository) {
        this.applicationRepository = theApplicationRepository;
        this.jobRepository = theJobRepository;
        this.userRepository = theUserRepository;

    }

    private ApplicationResponse mapToResponse(Application application) {

        // application -> application response
        User user = application.getUser();
        Job job = application.getJob();
        Company company = job.getCompany();

        return new ApplicationResponse(
                application.getId(),
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                job.getId(),
                job.getTitle(),
                company.getId(),
                company.getName(),
                application.getStatus(),
                application.getAppliedDate(),
                application.getNotes(),
                application.getCreatedAt());
    }

    @Override
    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {

        User user = userRepository.findById(applicationRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User is not found: " + applicationRequest.getUserId()));

        Job job = jobRepository.findById(applicationRequest.getJobId())
                .orElseThrow(() -> new JobNotFoundException("job is not found: " + applicationRequest.getJobId()));

        boolean alreadyExists = applicationRepository.existsByUserIdAndJobId(
                applicationRequest.getUserId(),
                applicationRequest.getJobId());

        if (alreadyExists) {
            // throw duplicate application exception
            throw new DuplicateApplicationException("User has already applied for this job");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        if (applicationRequest.getAppliedDate() != null) {
            application.setAppliedDate(applicationRequest.getAppliedDate());
        } else {
            application.setAppliedDate(LocalDate.now());
        }
        application.setNotes(applicationRequest.getNotes());
        application.setStatus(applicationRequest.getStatus());

        // save to DB
        Application savedApplication = applicationRepository.save(application);

        // application -> application response

        return mapToResponse(savedApplication);
    }

    @Override
    public List<ApplicationResponse> getAllApplications() {
        List<Application> applications = applicationRepository.findAll();
        return applications.stream().map(this::mapToResponse).toList();

    }

    @Override
    public ApplicationResponse getApplicationById(Long id) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new ApplicationNotFoundException(
                "Application not found with id: " + id));
        return mapToResponse(application);
    }

    @Override
    public ApplicationResponse updateApplication(Long id, ApplicationRequest applicationRequest) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("application is not found: " + id));

        // update application in DB
        User user = userRepository.findById(applicationRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("user is not found: " + applicationRequest.getUserId()));

        Job job = jobRepository.findById(applicationRequest.getJobId())
                .orElseThrow(() -> new JobNotFoundException("job is not found: " + applicationRequest.getJobId()));

        application.setUser(user);
        application.setJob(job);
        if (applicationRequest.getAppliedDate() != null) {
            application.setAppliedDate(applicationRequest.getAppliedDate());
        }
        application.setNotes(applicationRequest.getNotes());
        application.setStatus(applicationRequest.getStatus());

        // update in DB
        Application updatedApplication = applicationRepository.save(application);
        return mapToResponse(updatedApplication);

    }

    // Get Applications By User Id
    @Override
    public List<ApplicationResponse> getApplicationsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user is not found: " + userId));

        List<Application> applications = applicationRepository.findByUserId(userId);
        return applications.stream().map(this::mapToResponse).toList();
    }

    @Override
    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("application is not found: " + id));

        applicationRepository.delete(application);
    }

    @Override
    public List<ApplicationResponse> getApplicationByJobId(Long jobId) {
        jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(
                        "Job not found with id: " + jobId));

        return applicationRepository.findByJobId(jobId).stream().map(this::mapToResponse).toList();
    }

}
