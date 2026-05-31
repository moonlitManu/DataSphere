package com.DataSphere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DataSphere.dtos.UserLoginDto;
import com.DataSphere.dtos.UserRegisterDto;
import com.DataSphere.dtos.UserVerifyDto;
import com.DataSphere.service.AuthService;

@RestController
@RequestMapping("/public")
public class authController{
    
    @Autowired
    private AuthService userService;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto userdto){
        return userService.createUser(userdto);
    }
    @GetMapping("/login")
    public ResponseEntity<?> logInUser(@RequestBody UserLoginDto userdDto){
        return userService.logInUser(userdDto);
    }
 
    // to verify the email 
    @PutMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody UserVerifyDto user){
        return new ResponseEntity<>(userService.verifyUserOtp(user.getUsername(), user.getOtp()), HttpStatus.ACCEPTED);
    }
    // logIn using refresh token
    @GetMapping("/getAcess")
    public ResponseEntity<?> getAccessToken(@RequestBody String refreshToken){
        return new ResponseEntity<>(userService.getAccessToken(refreshToken), HttpStatus.CREATED);
    }

    // req password update 
    @GetMapping("/updatePassword/{email}")
    public ResponseEntity<?> reqPasswordUpdate(@PathVariable String email){
        return userService.reqPasswordUpdate(email);
    }

    // to update password
    @PutMapping("/updatePassword/{email}/{otp}")
    public ResponseEntity<?> updatePassword(@PathVariable String email, @PathVariable String otp, @RequestBody String newPassword){
        return userService.updatePassword(email, otp, newPassword);
    }
}
