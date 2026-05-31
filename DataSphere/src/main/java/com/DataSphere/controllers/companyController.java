package com.DataSphere.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DataSphere.entity.Company;
import com.DataSphere.entity.User;
import com.DataSphere.entity.UserCompany;
import com.DataSphere.repo.CompanyRepo;
import com.DataSphere.repo.UserCompanyRepo;
import com.DataSphere.repo.UserRepo;
import com.DataSphere.service.CompanyService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/company")
public class companyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserCompanyRepo userCompanyRepo;
    @Autowired
    private CompanyRepo companyRepo;

    @GetMapping("/createInvite")
    public ResponseEntity<?> inviteMember(HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            User user = userRepo.findByUsername(username);
            List<UserCompany> userCompanyList = userCompanyRepo.findByUId(user.getUId());
            UserCompany userCompany = userCompanyList.get(0);
            if (userCompany.getCompanyRole().equals("Owner")) {
                Company company = companyRepo.findById(userCompany.getCompanyId())
                        .orElseThrow(() -> new RuntimeException("Company not found"));
                ;
                return new ResponseEntity<>(companyService.inviteMember(company), HttpStatus.OK);
            }
            return new ResponseEntity<>("Can't create Invite", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    // to join the company
    @PostMapping("/joinCompany/{inviteCode}")
    public ResponseEntity<?> joinCompany(@PathVariable String inviteCode, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return companyService.joinCompany(inviteCode, username);
    }

    // controller to get all the members of the company
}
