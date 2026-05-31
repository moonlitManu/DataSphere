package com.DataSphere.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class health {
    
    @GetMapping("/health")
    public String healths(){
        return "The backend is working";
    }
}
