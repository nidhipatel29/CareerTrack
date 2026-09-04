package com.CareerTrack.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.CareerTrack.entity.Application;
import com.CareerTrack.entity.Company;
import com.CareerTrack.dto.ApplicationRequest;
import com.CareerTrack.dto.ApplicationResponse;
import com.CareerTrack.entity.Job;
import com.CareerTrack.entity.User;
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
            application.getCreatedAt()
    );
    }

    @Override
    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {

        User user = userRepository.findById(applicationRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User is not found: " + applicationRequest.getUserId()));

        Job job = jobRepository.findById(applicationRequest.getJobId())
                .orElseThrow(() -> new JobNotFoundException("job is not found: " + applicationRequest.getJobId()));

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllApplications'");
    }

    @Override
    public ApplicationResponse getApplicationById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getApplicationById'");
    }

    @Override
    public ApplicationResponse updateApplication(Long id, ApplicationRequest applicationRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateApplication'");
    }

    @Override
    public void deleteApplication(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteApplication'");
    }

}
