package com.CareerTrack.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.CareerTrack.entity.Company;
import com.CareerTrack.entity.EmploymentType;
import com.CareerTrack.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>{

    List<Job> findByTitleContainingIgnoreCase(String title);

    List<Job> findByLocationIgnoreCase(String location);
    
    List<Job> findByEmploymentType(EmploymentType employmentType);

    List<Job> findByCompanyId(Long companyId);
}
