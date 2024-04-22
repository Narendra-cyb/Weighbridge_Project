package com.weighbridge.services.impls;

import com.weighbridge.dtos.CompanyMasterDto;
import com.weighbridge.entities.CompanyMaster;
import com.weighbridge.exceptions.ResourceNotFoundException;
import com.weighbridge.exceptions.ResourceRetrievalException;
import com.weighbridge.repsitories.CompanyMasterRepository;
import com.weighbridge.services.CompanyMasterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyMasterServiceImpl implements CompanyMasterService {

    public final CompanyMasterRepository companyMasterRepository;
    public final ModelMapper modelMapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public CompanyMasterDto createCompany(CompanyMasterDto companyMasterDto) {
        try {
            // Check if the company name already exists
            CompanyMaster existingCompany = companyMasterRepository.findByCompanyName(companyMasterDto.getCompanyName());
            if (existingCompany != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists");
            }

            // Set user session details

            HttpSession session = request.getSession();
            String userId;
            if (session != null && session.getAttribute("userId") != null) {
                userId = session.getAttribute("userId").toString();
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session Expired, Login again !");
            }
            companyMasterDto.setCompanyCreatedBy(userId);
            companyMasterDto.setCompanyModifiedBy(userId);
            companyMasterDto.setCompanyCreatedDate(LocalDateTime.now());
            companyMasterDto.setCompanyModifiedDate(LocalDateTime.now());

            // Map DTO to entity and save
            CompanyMaster company = modelMapper.map(companyMasterDto, CompanyMaster.class);
            CompanyMaster savedCompany = companyMasterRepository.save(company);

            return modelMapper.map(savedCompany, CompanyMasterDto.class);
        } catch (ResponseStatusException e) {
            // If the company name already exists, rethrow the exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Session Expired, Login again");
        } catch (Exception e) {
            // If any other unexpected error occurs, handle it and provide a generic error message
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }


    private String generateCompanyId(String companyName) {
        String companyAbbreviation = "";
        if (companyName.length() >= 3) {
            companyAbbreviation = companyName.substring(0, 1).toUpperCase() + companyName.substring(2, 3).toUpperCase();
        } else if (companyName.length() == 2) {
            companyAbbreviation = companyName.substring(0, 1).toUpperCase() + companyName.substring(1, 2).toUpperCase();
        } else {
            // If the company name has only one letter, just take the first letter
            companyAbbreviation = companyName.substring(0, 1).toUpperCase();
        }
        // Concatenate the abbreviation and unique identifier
        long siteCount = companyMasterRepository.countByCompanyNameStartingWith(companyAbbreviation);

        // Generate the site ID based on the count
        String companyId;
        if (siteCount > 0) {
            // If other sites with the same abbreviation exist, append a numeric suffix
            companyId = String.format("%s%02d", companyAbbreviation, siteCount + 1);
        } else {
            // Otherwise, use the abbreviation without a suffix
            companyId = companyAbbreviation + "01";
        }

        return companyId;
    }

    @Override
    public List<CompanyMasterDto> getAllCompany() {
        List<CompanyMaster> companies = companyMasterRepository.findAll();
        return companies.stream().map(company -> modelMapper.map(company, CompanyMasterDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCompanyNameOnly() {

        try {
            List<String> allCompanyListName = companyMasterRepository.findAllCompanyListName();
            return allCompanyListName;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to retrieve roles");
        }

    }
}
