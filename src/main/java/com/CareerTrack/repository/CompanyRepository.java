package com.CareerTrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.CareerTrack.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @EntityGraph(attributePaths = "jobs")
    @Query("SELECT c FROM Company c")
    List<Company> findAllWithJobs();

    Company findByJobId(Long id);

}
