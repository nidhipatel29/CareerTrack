package com.CareerTrack.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CareerTrack.dto.ApplicationResponse;
import com.CareerTrack.entity.Application;
public interface ApplicationRepository extends JpaRepository<Application,Long>{
    
    List<Application> findByUserId(Long userId);

}
