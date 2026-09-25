package com.CareerTrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CareerTrack.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @EntityGraph(attributePaths = {
            "user",
            "job",
            "job.company"
    })
    List<Application> findByUserId(Long userId);

     @EntityGraph(attributePaths = {
            "user",
            "job",
            "job.company"
    })
    List<Application> findByJobId(Long jobId);

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    boolean existsByJobId(Long jobId);

}
