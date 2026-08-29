
package com.CareerTrack.dto;

import java.time.LocalDateTime;

public class CompanyResponse {

    private Long id;
    private String name;
    private String description;
    private String website;
    private String location;
    private LocalDateTime createdAt;

    public CompanyResponse(){

    }
    public CompanyResponse(Long id,String name, String description, String website, String location,LocalDateTime createdAt) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.location = location;
        this.createdAt=createdAt;
    }

     //getter setter

    public Long getId() {
        return id;
    }

    public void setId(Long id){
      this.id=id;
    }
   
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
     public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt=createdAt;  
    }
    

    
}