package com.CareerTrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.CareerTrack.entity.Application;
public interface ApplicationRepository extends JpaRepository<Application,Long>{
    
}
