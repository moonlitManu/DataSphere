package com.DataSphere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DataSphere.entity.Company;
import com.DataSphere.entity.User;
import com.DataSphere.entity.UserCompany;
import com.DataSphere.repo.CompanyRepo;
import com.DataSphere.repo.UserCompanyRepo;
import com.DataSphere.repo.UserRepo;


@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired 
    private UserCompanyRepo userCompanyRepo;
    @Autowired
    private CompanyRepo companyRepo;

    @Transactional
    public ResponseEntity<?> createCompany(String company_name, String username) {
        try {
            User user = userRepo.findByUsername(username);
            Company company = new Company();
            company.setCompanyName(company_name);
            company.setOwnerId(user.getUId());
            Company companydb = companyRepo.save(company);
            UserCompany userCompany = new UserCompany();
            userCompany.setCompanyId(companydb.getCompanyId());
            userCompany.setCompanyName(company_name);
            userCompany.setUId(user.getUId());
            userCompany.setCompanyRole("Owner");
            userCompanyRepo.save(userCompany);

            return new ResponseEntity<>("Company is created", HttpStatus.CREATED);

        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> joinCompany(String inviteCode, String username) {
        User user = userRepo.findByUsername(username);
        Company company = companyRepo.findByInviteCode(inviteCode);
        UserCompany userCompany = new UserCompany();
        userCompany.setCompanyRole("Member");
        userCompany.setUId(user.getUId());
        userCompany.setCompanyId(company.getCompanyId());
        userCompany.setCompanyName(company.getCompanyName());
        userCompanyRepo.save(userCompany);

        return new ResponseEntity<>("Joined Successfully", HttpStatus.OK);
    }
    
}
