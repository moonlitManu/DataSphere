package com.DataSphere.dtos;

import lombok.Data;

@Data
public class UserVerifyDto {
    private String username;
    private String otp;
}
