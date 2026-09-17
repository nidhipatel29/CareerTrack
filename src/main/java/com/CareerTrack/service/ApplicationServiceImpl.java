package com.CareerTrack.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import com.CareerTrack.entity.Application;
import com.CareerTrack.entity.Company;
import com.CareerTrack.dto.ApplicationRequest;
import com.CareerTrack.dto.ApplicationResponse;
import com.CareerTrack.dto.ApplicationStatusUpdateRequest;
import com.CareerTrack.entity.Job;
import com.CareerTrack.entity.Role;
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

    private boolean isAdmin(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(User::getRole)
                .filter(role -> role == Role.ADMIN)
                .isPresent();
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
    public ApplicationResponse createApplication(ApplicationRequest applicationRequest, String email) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User is not found:"));

        Job job = jobRepository.findById(applicationRequest.getJobId())
                .orElseThrow(() -> new JobNotFoundException(
                        "Job is not found: " + applicationRequest.getJobId()));

        boolean alreadyExists = applicationRepository.existsByUserIdAndJobId(
                user.getId(),
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
    public List<ApplicationResponse> getMyApplications(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found wit this mail: " + email));
        Long id = user.getId();
        List<Application> applications = applicationRepository.findByUserId(id);
        return applications.stream().map(this::mapToResponse).toList();

    }

    @Override
    public ApplicationResponse getApplicationById(Long id, String email) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new ApplicationNotFoundException(
                "Application not found with id: " + id));

        String userEmail = application.getUser().getEmail();

        if (isAdmin(email) || userEmail.equalsIgnoreCase(email)) {
            return mapToResponse(application);

        } else {
            throw new AccessDeniedException("you do not have access to this id:" + id);
        }
    }

    @Override
    public ApplicationResponse updateApplication(Long id, ApplicationRequest applicationRequest, String email) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("application is not found: " + id));

        String userName = application.getUser().getEmail();
        if (isAdmin(email) || userName.equalsIgnoreCase(email)) {

            // update application in DB

            if (applicationRequest.getAppliedDate() != null) {
                application.setAppliedDate(applicationRequest.getAppliedDate());
            }
            application.setNotes(applicationRequest.getNotes());
            application.setStatus(applicationRequest.getStatus());

            // update in DB
            Application updatedApplication = applicationRepository.save(application);
            return mapToResponse(updatedApplication);

        } else {
            throw new AccessDeniedException("access is denied for this user:" + email);
        }

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
    public void deleteApplication(Long id, String email) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("application is not found: " + id));

        String userName = application.getUser().getEmail();
        if (isAdmin(email) || userName.equalsIgnoreCase(email)) {
            applicationRepository.delete(application);

        } else {
            throw new AccessDeniedException("Access is denied for this id: " + id);
        }

    }

    @Override
    public List<ApplicationResponse> getApplicationByJobId(Long jobId, String email) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(
                        "Job not found with id: " + jobId));

        String employerEmail = job.getCompany()
                .getEmployer()
                .getEmail();

        if (!isAdmin(email) && !employerEmail.equalsIgnoreCase(email)) {
            throw new AccessDeniedException(
                    "You do not have access to applications for this job");
        }

        return applicationRepository.findByJobId(jobId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatusUpdateRequest request,
            String email) {

        // 1. Find the application
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(
                        "Application not found with id: " + applicationId));

        // 2. Get the employer who owns the job
        String employerEmail = application.getJob()
                .getCompany()
                .getEmployer()
                .getEmail();

        // 3. Check ownership
        if (!employerEmail.equalsIgnoreCase(email)) {
            throw new AccessDeniedException(
                    "You can update application status only for your own jobs");
        }

        // 4. Update only the status
        application.setStatus(request.getStatus());

        // 5. Save
        Application updatedApplication = applicationRepository.save(application);

        // 6. Return response DTO
        return mapToResponse(updatedApplication);
    }

}
