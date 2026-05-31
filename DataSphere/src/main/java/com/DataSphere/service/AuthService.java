package com.DataSphere.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DataSphere.dtos.UserLoginDto;
import com.DataSphere.dtos.UserRegisterDto;
import com.DataSphere.entity.User;
import com.DataSphere.repo.UserRepo;
import com.DataSphere.utils.JwtUtils;

@Service
public class AuthService {
    
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;
    public ResponseEntity<?> createUser(UserRegisterDto userdto){
        try {
            if(userRepo.findByUsername(userdto.getUsername()) != null){
                return new ResponseEntity<>("username already exist", HttpStatus.BAD_REQUEST);
            }
            User user = new User();
            user.setUsername(userdto.getUsername());
            user.setPassword(passwordEncoder.encode(userdto.getPassword()));
            user.setEmail(userdto.getEmail());
            user.setCreationDate(LocalDate.now());
            user.setRole("USER");
            user.setVerified(false);
            user.setExpiryOtpTime(LocalDateTime.now().plusMinutes(15));
            user.setOtp(generateOtp());
            // to send email to the user with otp
            emailService.sendOtp(user.getEmail(), user.getOtp(), "To verify profile otp is: ");
            userRepo.save(user);

            return new ResponseEntity<>("User created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("User not created", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> logInUser(UserLoginDto userdDto) {
        try {
            User user = new User();
            if(userdDto.getIdentifier().contains("@")){
                user = userRepo.findByEmail(userdDto.getIdentifier());
            }
            user = userRepo.findByUsername(userdDto.getIdentifier());
            if(!user.isVerified()){
                return new ResponseEntity<>("User not verfied", HttpStatus.BAD_REQUEST);
            }
            if(passwordEncoder.matches(userdDto.getPassword(), user.getPassword())){
                return  ResponseEntity.status(HttpStatus.CREATED).body(jwtUtils.login(user));
            }else{
                throw new Exception("failed to generate token");
            }
        } catch (Exception e) {
            return new ResponseEntity<>("failed to genereate token", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAccessToken(String refreshToken) {
        if(jwtUtils.isValidRefreshToken(refreshToken)){
            return new ResponseEntity<>( jwtUtils.generateAccessToken(refreshToken), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed to cread access Token", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> verifyUserOtp(String email, String otp) {
        User user = userRepo.findByUsername(email);
        if(user.getOtp().equals(otp) && user.getExpiryOtpTime().isAfter(LocalDateTime.now())){
            user.setOtp(null);
            user.setExpiryOtpTime(null);
            user.setVerified(true);
            userRepo.save(user);
            return new ResponseEntity<>("User is verified now", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not verified",HttpStatus.BAD_REQUEST);
    }

    // function to genereate random otp
    public String generateOtp(){
        String otp =  String.valueOf(
            new Random().nextInt(900000) + 100000
        );
        return otp;
    }

    // to update the password
    public ResponseEntity<?> reqPasswordUpdate(String email) {
       try {
        User user = userRepo.findByEmail(email);
        String otp = generateOtp();
        user.setOtp(otp);
        user.setExpiryOtpTime(LocalDateTime.now().plusMinutes(15));
        userRepo.save(user);
        emailService.sendOtp(email, otp, "To update the password otp is: ");
        return new ResponseEntity<>("Check email for otp to update password", HttpStatus.OK);
       } catch (Exception e) {
        return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
       }
    }

    public ResponseEntity<?> updatePassword(String email, String otp, String newPassword) {
        try {
            User user = userRepo.findByEmail(email);
            if(user.getOtp().equals(otp) && user != null){
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setOtp(null);
                userRepo.save(user);
                return new ResponseEntity<>("Password updated", HttpStatus.OK);
            }
            return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    

}
