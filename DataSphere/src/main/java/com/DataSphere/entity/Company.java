package com.DataSphere.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

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
public class Company {
    @Column(unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID companyId; // keep it protected don't share it in frontend
    private String companyName;
    private UUID ownerId;
    @CreationTimestamp
    private Date creationDate;

    // to invite other to company using temporary token
    @Column(unique = true)
    private String inviteCode;
    private LocalDateTime inviteExpiry;
}
