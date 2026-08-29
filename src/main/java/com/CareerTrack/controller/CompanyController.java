package com.CareerTrack.controller;

import org.springframework.web.bind.annotation.RestController;

import com.CareerTrack.service.CompanyService;

@RestController
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService theCompanyService){
      this.companyService=theCompanyService;
    }
    
}
