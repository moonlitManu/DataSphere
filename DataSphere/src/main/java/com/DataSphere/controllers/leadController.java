package com.DataSphere.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DataSphere.dtos.LeadDto;
import com.DataSphere.service.LeadService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lead")
public class leadController {
    
    @Autowired
    private LeadService leadService;
    // to create lead
    @PostMapping("/createLead")
    public ResponseEntity<?> createLead(@RequestBody LeadDto leadDto, HttpServletRequest request){
        String username = (String)request.getAttribute("username");
        return leadService.createLead(leadDto, username);
    }

    // to read lead
    @GetMapping("/getLeads")
    public ResponseEntity<?> getLead(HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        return new ResponseEntity<>(leadService.getLead(username), HttpStatus.OK);
    }

    // to delete the lead 
    @DeleteMapping("/deleteLead/{leadId}")
    public ResponseEntity<?> deleteLead(@PathVariable UUID leadId){
        return new ResponseEntity<>(leadService.deleteLead(leadId), HttpStatus.OK);
    }

    // to update the lead
    @PutMapping("/updateLead/{leadId}")
    public ResponseEntity<?> updateLead(@RequestBody LeadDto leadDto, @PathVariable UUID leadId){
        return leadService.updateLead(leadDto, leadId);
    }

    // to assign lead to the user
    @PutMapping("/assignLead/{username}/{leadId}")
    public ResponseEntity<?> assignLead(@PathVariable String username, @PathVariable UUID leadId){
        return leadService.assignLead(username, leadId);
    }
}
