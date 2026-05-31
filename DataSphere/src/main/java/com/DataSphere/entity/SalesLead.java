package com.DataSphere.entity;

import java.util.UUID;

import com.DataSphere.enums.LeadStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesLead {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID leadId;
    private String name;
    private int phone;
    private String email;
    private String budget;
    private String source;
    private LeadStatus status;
    private String notes;
    private UUID companyId; // keep this protected don't share it in forntend
    private UUID assignedTo;
}
