package com.CareerTrack.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.CareerTrack.dto.JobRequest;
import com.CareerTrack.dto.JobResponse;
import com.CareerTrack.entity.EmploymentType;
import com.CareerTrack.service.JobService;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

  @GetMapping("")
  public List<JobResponse> viewAllJobs() {
    return jobService.getJobs();

  }

  @GetMapping("{id}")
  public JobResponse getJobById(@PathVariable Long id) {
    return jobService.getJobById(id);

  }

  @GetMapping("/search")
  public List<JobResponse> searchJobsByTitle(
      @RequestParam String title) {

    return jobService.searchJobsByTitle(title);
  }

  @GetMapping("/filter")
  public List<JobResponse> filterJobByLocation(@RequestParam String location) {
    return jobService.searchJobByLocation(location);
  }

  @GetMapping("/filter/employment-type")
  public List<JobResponse> filterJobByEmployementType(@RequestParam EmploymentType employment) {
    return jobService.filterJobByEmployementType(employment);
  }

  @GetMapping("/filter/company_id")
  public List<JobResponse> filterJobByCompanyId(@RequestParam Long id) {
    return jobService.filterByCompanyId(id);
  }

  @GetMapping("/filters")
  public List<JobResponse> filterJobs(
      @RequestParam(required = false) String location,
      @RequestParam(required = false) EmploymentType employmentType,
      @RequestParam(required = false) Long companyId) {

    // service call
    return  jobService.filterJobs(location, employmentType, companyId).stream().toList();
  }

@GetMapping("/page")
public Page<JobResponse> getSelectedJobs(
        @RequestParam int page,
        @RequestParam int size) {

    return jobService.getJobsWithPagination(page, size);
}
  

  @PutMapping("{id}")
  public ResponseEntity<JobResponse> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest jobRequest) {
    JobResponse jobResponse = jobService.updateJob(id, jobRequest);
    return ResponseEntity.ok(jobResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
    jobService.deleteJob(id);
    return ResponseEntity.noContent().build();
  }

  

}
