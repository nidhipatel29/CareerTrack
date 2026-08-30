package com.CareerTrack.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.CareerTrack.dto.CompanyRequest;
import com.CareerTrack.dto.CompanyResponse;
import com.CareerTrack.entity.Company;
import com.CareerTrack.exception.CompanyNotFoundException;
import com.CareerTrack.repository.CompanyRepository;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository theCompanyRepository) {
        this.companyRepository = theCompanyRepository;
    }

    private CompanyResponse mapToResponse(Company company) {

        // convert Company entity → CompanyResponse DTO
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getWebsite(),
                company.getLocation(),
                company.getCreatedAt());

    }

    @Override
    public CompanyResponse createCompany(CompanyRequest request) {

        // now we need to save the entity not a request object
        // convert companyRequest to company entity
        Company company = new Company(
                request.getName(), request.getDescription(), request.getWebsite(), request.getLocation());

        Company savedCompany = companyRepository.save(company);

        // return response
        // convert company entity to company response
         return mapToResponse(savedCompany);
    }

    @Override
    public List<CompanyResponse> getCompanies() {

        List<Company> companies = companyRepository.findAll();
        List<CompanyResponse> companyResponses = new ArrayList<>();

        for (Company company : companies) {
            CompanyResponse companyResponse = mapToResponse(company);
            companyResponses.add(companyResponse);
        }
        return companyResponses;
    }

    @Override
    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));

               return mapToResponse(company);

    }

    @Override
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));

        company.setName(request.getName());
        company.setDescription(request.getDescription());
        company.setWebsite(request.getWebsite());
        company.setLocation(request.getLocation());

        Company updatedCompany = companyRepository.save(company);
        return mapToResponse(updatedCompany);
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));

        companyRepository.delete(company);
    }

}
