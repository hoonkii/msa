package com.example.userservice.vo;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotNull(message = "email cannot be null")
    private String email;
    private String name;
    private String pwd;
}
