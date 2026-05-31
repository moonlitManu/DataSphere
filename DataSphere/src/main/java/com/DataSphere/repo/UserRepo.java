package com.DataSphere.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DataSphere.entity.User;

public interface UserRepo extends JpaRepository<User, UUID>{
    public User findByUsername(String username);
    public User findByEmail(String email);
}
