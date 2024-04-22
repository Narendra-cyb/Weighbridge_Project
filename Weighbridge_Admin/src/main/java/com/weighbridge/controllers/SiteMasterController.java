package com.weighbridge.controllers;

import com.weighbridge.dtos.SiteMasterDto;
import com.weighbridge.payloads.SiteRequest;
import com.weighbridge.services.SiteMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sites")
public class SiteMasterController {

    private final SiteMasterService siteMasterService;

    /**
     * Endpoint for saving site and assign to a company
     * @param siteRequest The payload containing site information to be saved.
     * @return String containing the message site saved successfully with HTTP status OK.
     */
    @PostMapping()
    public ResponseEntity<String> createSite(@Validated @RequestBody SiteRequest siteRequest){
        String response = siteMasterService.createSite(siteRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<SiteMasterDto>> GetAllSites(){
        List<SiteMasterDto> savedSites = siteMasterService.getAllSite();
        return ResponseEntity.ok(savedSites);
    }



    // todo: create method to get all the sites as a list of strings
    @GetMapping("/company/{companyName}")
    public ResponseEntity<List<Map<String, String>>> GetAllSitesOfCompany(@PathVariable String companyName){
        List<Map<String, String>> allByCompanySites = siteMasterService.findAllByCompanySites(companyName);
        return ResponseEntity.ok(allByCompanySites);
    }
}