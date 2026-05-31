package com.DataSphere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DataSphere.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class userController {
    
    @Autowired
    private UserService userService;
    // to create company
    @GetMapping("/createCompany/{companyName}")
    public ResponseEntity<?> createCompany(@PathVariable String companyName, Authentication authentication){
        String username = authentication.getName();
        return userService.createCompany(companyName, username);
    }

    // to join the company
    @PostMapping("/joinCompany/{inviteCode}")
    public ResponseEntity<?> joinCompany(@PathVariable String inviteCode, HttpServletRequest request){
        String username = (String)request.getAttribute("username");
        return userService.joinCompany(inviteCode, username);
    }
    
}
