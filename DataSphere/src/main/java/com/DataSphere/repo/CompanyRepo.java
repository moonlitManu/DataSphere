package com.DataSphere.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DataSphere.entity.Company;

public interface CompanyRepo extends JpaRepository<Company, UUID>{
    public Company findByInviteCode(String inviteCode);
}
