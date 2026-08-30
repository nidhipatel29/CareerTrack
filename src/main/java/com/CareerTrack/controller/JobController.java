package com.CareerTrack.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.CareerTrack.dto.JobRequest;
import com.CareerTrack.dto.JobResponse;
import com.CareerTrack.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private JobService jobService;
    

     public JobController(JobService jobService) {
        this.jobService = jobService;
    }


 @PostMapping
  public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest jobRequest) {
    JobResponse theJobResponse = jobService.createJob(jobRequest);

    // converting jobResonce to ResponseEntity
    return ResponseEntity.status(HttpStatus.CREATED).body(theJobResponse);

  }
    
    
}
