package com.example.userservice.vo;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
