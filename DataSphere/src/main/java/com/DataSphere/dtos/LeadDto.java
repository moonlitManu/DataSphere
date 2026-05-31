package com.DataSphere.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadDto {
    private String name;
    private int phone;
    private String email;
    private String budget;
    private String source;
    private String notes;
    private UUID assignedTo;

}
