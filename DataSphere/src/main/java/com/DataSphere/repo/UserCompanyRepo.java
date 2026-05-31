package com.DataSphere.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.DataSphere.entity.UserCompany;

public interface UserCompanyRepo extends JpaRepository<UserCompany, UUID> {
    @Query("SELECT u FROM UserCompany u WHERE u.uId = :uId")
    public List<UserCompany> findByUId(@Param("uId")UUID uId);
}
