package com.CareerTrack.service;

import java.util.List;

import com.CareerTrack.dto.CompanyRequest;
import com.CareerTrack.dto.CompanyResponse;

public interface CompanyService {
    
    CompanyResponse createCompany(CompanyRequest request,String email);

    List<CompanyResponse> getCompanies();

    CompanyResponse getCompanyById(Long id);

    CompanyResponse updateCompany(Long id, CompanyRequest request, String email);

    void deleteCompany(Long id);
}
