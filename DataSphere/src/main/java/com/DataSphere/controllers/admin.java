package com.DataSphere.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class admin {
    
    @GetMapping("/check")
    public String check(){
        return "Admin controller is working";
    }
}
