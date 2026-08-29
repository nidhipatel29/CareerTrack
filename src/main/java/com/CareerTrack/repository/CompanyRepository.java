package com.CareerTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CareerTrack.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
    
}
