package com.DataSphere.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.DataSphere.entity.SalesLead;

public interface SalesLeadRepo extends JpaRepository<SalesLead, UUID>{
    @Query("SELECT u from SalesLead u WHERE u.companyId = :companyId")
    List<SalesLead> getSalesLeadByCompanyId(@Param("companyId") UUID companyId);
}
