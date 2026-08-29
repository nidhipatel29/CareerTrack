package com.CareerTrack.service;

import org.springframework.stereotype.Service;

import com.CareerTrack.dto.CompanyRequest;
import com.CareerTrack.dto.CompanyResponse;
import com.CareerTrack.entity.Company;
import com.CareerTrack.repository.CompanyRepository;

@Service
public class CompanyServiceImpl implements CompanyService {


    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository theCompanyRepository){
      this.companyRepository=theCompanyRepository;
    }
    @Override
    public CompanyResponse createCompany(CompanyRequest request) {

        //now we need to save the entity not a request object
        //convert companyRequest to company entity 
        Company company=new Company(
                                    request.getName(),request.getDescription(),request.getWebsite(),request.getLocation());
    
        
        Company tempCompany=companyRepository.save(company);

        //return response
        //convert company entity to company response

        CompanyResponse companyResponse=new CompanyResponse(tempCompany.getId(),tempCompany.getName(),tempCompany.getDescription()
                                                            ,tempCompany.getWebsite(),tempCompany.getLocation(),tempCompany.getCreatedAt());
        return companyResponse;
    }
    
}
