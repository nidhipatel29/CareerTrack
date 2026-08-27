package com.CareerTrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.CareerTrack.Application;

public interface ApplicationRepository extends JpaRepository<Application,Long>{
    
}
