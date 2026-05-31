package com.DataSphere.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${emailService.email}")
    private String CRMemail;
    @Value("${emailService.appPassword}")
    private String appPassword;
    private Session session;

    @PostConstruct
    public void init() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");

        session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CRMemail, appPassword);
            }
        });
    }

    public void sendOtp(String recepientEmail, String otp, String reqMsg) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(CRMemail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepientEmail));
            message.setSubject("Verification otp for CRM");
            message.setText(reqMsg + otp);
            Transport.send(message);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

}
