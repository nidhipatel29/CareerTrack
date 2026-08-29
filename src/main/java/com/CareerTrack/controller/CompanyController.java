package com.CareerTrack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CareerTrack.dto.CompanyRequest;
import com.CareerTrack.dto.CompanyResponse;
import com.CareerTrack.service.CompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService theCompanyService){
      this.companyService=theCompanyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest companyRequest){
         CompanyResponse theCompanyResponse=  companyService.createCompany(companyRequest);

         //converting companyResonce to ResponseEntity
         return ResponseEntity.status(HttpStatus.CREATED).body(theCompanyResponse);

    }
    
}
