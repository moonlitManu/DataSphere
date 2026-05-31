package com.DataSphere.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.DataSphere.entity.Company;
import com.DataSphere.entity.User;
import com.DataSphere.entity.UserCompany;
import com.DataSphere.repo.CompanyRepo;
import com.DataSphere.repo.UserCompanyRepo;
import com.DataSphere.repo.UserRepo;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepo companyRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserCompanyRepo userCompanyRepo;

    // to invite the user to company
    public String inviteMember(Company company) {
        String invite = String.valueOf(
                new Random().nextInt(900000) + 100000);
        company.setInviteCode(invite);
        company.setInviteExpiry(LocalDateTime.now().plusHours(12));
        companyRepo.save(company);

        return invite;
    }

    public ResponseEntity<?> joinCompany(String inviteCode, String username) {
        try {
            User user = userRepo.findByUsername(username);
            Company company = companyRepo.findByInviteCode(inviteCode);
            UserCompany userCompany = new UserCompany();
            userCompany.setCompanyId(company.getCompanyId());
            userCompany.setCompanyName(company.getCompanyName());
            userCompany.setCompanyRole("MEMBER");
            userCompany.setUId(user.getUId());
            userCompanyRepo.save(userCompany);
            return new ResponseEntity<>("Joined successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occured while joining the company", HttpStatus.CONFLICT);
        }
    }

    public UUID getCompanyIdByUsername(String username){
        User user = userRepo.findByUsername(username);
        List<UserCompany> userCompanies = userCompanyRepo.findByUId(user.getUId());
        UserCompany userCompany = userCompanies.get(0);
        return userCompany.getCompanyId();
    }
}
