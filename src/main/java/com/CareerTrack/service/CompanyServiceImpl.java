package com.CareerTrack.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.CareerTrack.dto.CompanyRequest;
import com.CareerTrack.dto.CompanyResponse;
import com.CareerTrack.entity.Company;
import com.CareerTrack.entity.Job;
import com.CareerTrack.entity.Role;
import com.CareerTrack.entity.User;
import com.CareerTrack.exception.CompanyHasJobsException;
import com.CareerTrack.exception.CompanyNotFoundException;
import com.CareerTrack.exception.UserNotFoundException;
import com.CareerTrack.repository.CompanyRepository;
import com.CareerTrack.repository.JobRepository;
import com.CareerTrack.repository.UserRepository;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private JobRepository jobRepository;

    public CompanyServiceImpl(CompanyRepository theCompanyRepository,UserRepository userRepository, JobRepository jobRepository) {
        this.companyRepository = theCompanyRepository;
        this.userRepository=userRepository;
        this.jobRepository = jobRepository;
    }

    private void validateEmployerOwnsCompany(String email, Company company,String action) {
        User currentUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (currentUser.getRole() != Role.EMPLOYER) {
            throw new AccessDeniedException("Only employers can" + action +"companies");
        }

        if (company.getEmployer() == null || !company.getEmployer().getEmail().equalsIgnoreCase(email)) {
            throw new AccessDeniedException("Employer can  only  " +  action + " their own company");
        }
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
    public CompanyResponse createCompany(CompanyRequest request,String email) {

        // now we need to save the entity not a request object
        // convert companyRequest to company entity
        Company company = new Company(
                request.getName(), request.getDescription(), request.getWebsite(), request.getLocation());

        User user=userRepository.findByEmailIgnoreCase(email).orElseThrow(()->new UserNotFoundException("user is  not found: "));
        company.setEmployer(user);
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
    public CompanyResponse updateCompany(Long id, CompanyRequest request, String email) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));

        validateEmployerOwnsCompany(email, company,"update");

        company.setName(request.getName());
        company.setDescription(request.getDescription());
        company.setWebsite(request.getWebsite());
        company.setLocation(request.getLocation());

        Company updatedCompany = companyRepository.save(company);
        return mapToResponse(updatedCompany);
    }

    @Override
    public void deleteCompany(Long id, String email) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));

        validateEmployerOwnsCompany(email, company,"delete");

        boolean hasJob = jobRepository.existsByCompanyId(id);
        if (hasJob) {
            throw new CompanyHasJobsException("Cannot delete company because it still has jobs associated with it");
        }

        companyRepository.delete(company);
    }

}
