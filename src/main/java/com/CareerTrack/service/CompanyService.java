package com.CareerTrack.service;

import java.util.List;

import com.CareerTrack.dto.CompanyRequest;
import com.CareerTrack.dto.CompanyResponse;

public interface CompanyService {
    
    CompanyResponse createCompany(CompanyRequest request);

    List<CompanyResponse> getCompanies();

    CompanyResponse getCompanyById(Long id);
}
