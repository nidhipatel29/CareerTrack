package com.CareerTrack.service;

import com.CareerTrack.dto.CompanyRequest;
import com.CareerTrack.dto.CompanyResponse;

public interface CompanyService {
    
    CompanyResponse createCompany(CompanyRequest request);
}
