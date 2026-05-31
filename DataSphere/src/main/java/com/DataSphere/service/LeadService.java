package com.DataSphere.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.DataSphere.dtos.LeadDto;
import com.DataSphere.entity.SalesLead;
import com.DataSphere.entity.User;
import com.DataSphere.enums.LeadStatus;
import com.DataSphere.repo.SalesLeadRepo;
import com.DataSphere.repo.UserRepo;

@Service
public class LeadService {

    @Autowired
    private SalesLeadRepo leadRepo;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserRepo userRepo;

    public ResponseEntity<?> createLead(LeadDto leadDto, String username) {
        try {
            SalesLead lead = new SalesLead();
            lead.setName(leadDto.getName());
            lead.setEmail(leadDto.getEmail());
            lead.setPhone(leadDto.getPhone());
            lead.setSource(leadDto.getSource());
            lead.setNotes(leadDto.getNotes());
            lead.setBudget(leadDto.getBudget());
            lead.setStatus(LeadStatus.NEW);
            lead.setCompanyId(companyService.getCompanyIdByUsername(username));
            leadRepo.save(lead);
            return new ResponseEntity<>("Lead created", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getLead(String username) {
        try {
            UUID companyid = companyService.getCompanyIdByUsername(username);
            List<SalesLead> leads = leadRepo.getSalesLeadByCompanyId(companyid);
            for(SalesLead lead : leads){
                lead.setCompanyId(null);
            }
            return new ResponseEntity<>(leads, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get lead", HttpStatus.BAD_REQUEST);
        }
    }

    public String deleteLead(UUID leadId) {
        try {
            leadRepo.deleteById(leadId);
            return "Lead Deleted succcessfully";
        } catch (Exception e) {
            return "failed to delet the lead";
        }
    }
    // to update the lead
    public ResponseEntity<?> updateLead(LeadDto leadDto, UUID leadId) {
        try {
            SalesLead lead = leadRepo.findById(leadId).orElseThrow(null);
            if(leadDto.getName() != null){
                lead.setName(leadDto.getName());
            }if(leadDto.getEmail() != null){
                lead.setEmail(leadDto.getEmail());
            }if(leadDto.getPhone() != lead.getPhone()){
                lead.setPhone(leadDto.getPhone());
            }if(leadDto.getBudget() != null){
                lead.setBudget(leadDto.getBudget());
            }if(leadDto.getAssignedTo() != null){
                lead.setAssignedTo(leadDto.getAssignedTo());
            }if(leadDto.getNotes() != null){
                lead.setNotes(leadDto.getNotes());
            }
            leadRepo.save(lead);
            return new ResponseEntity<>(lead, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update lead", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> assignLead(String username, UUID leadId) {
        try {
            SalesLead lead = leadRepo.findById(leadId).orElseThrow(null);
            User user = userRepo.findByUsername(username);
            lead.setAssignedTo(user.getUId());
            leadRepo.save(lead);
            return new ResponseEntity<>("Lead assigned",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to assign lead", HttpStatus.CONFLICT);
        }
    }

}
