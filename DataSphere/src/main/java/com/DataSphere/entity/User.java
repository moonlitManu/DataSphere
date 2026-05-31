package com.DataSphere.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
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
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uId;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private LocalDate creationDate = LocalDate.now();
    private String role;
    // for otp based verification 
    @Column(nullable = false)
    private boolean isVerified = false;
    private String otp;
    private LocalDateTime expiryOtpTime;
}
