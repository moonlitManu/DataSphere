package com.DataSphere.dtos;

import lombok.Data;

@Data
public class UserLoginDto {
    private String identifier;
    private String password;
}
