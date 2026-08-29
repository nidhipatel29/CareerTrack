package com.CareerTrack.controller;

import java.util.List;
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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

  private CompanyService companyService;

  public CompanyController(CompanyService theCompanyService) {
    this.companyService = theCompanyService;
  }

  @PostMapping
  public ResponseEntity<CompanyResponse> startComapny(@Valid @RequestBody CompanyRequest companyRequest) {
    CompanyResponse theCompanyResponse = companyService.createCompany(companyRequest);

    // converting companyResonce to ResponseEntity
    return ResponseEntity.status(HttpStatus.CREATED).body(theCompanyResponse);

  }

  @GetMapping("")
  public List<CompanyResponse> viewAllCompanies() {
    List<CompanyResponse> retrievedCompanies = companyService.getCompanies();
    return retrievedCompanies;

  }

  @GetMapping("/{id}")
  public CompanyResponse viewAllCompanyById(@PathVariable Long id) {
    return companyService.getCompanyById(id);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id,
      @Valid @RequestBody CompanyRequest request) {
    CompanyResponse updatedCompany = companyService.updateCompany(id, request);
    return ResponseEntity.ok(updatedCompany);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
    companyService.deleteCompany(id);
    return ResponseEntity.noContent().build();
  }

}
