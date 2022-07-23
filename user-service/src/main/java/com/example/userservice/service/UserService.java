package com.example.userservice.service;


import com.example.userservice.dto.UserDto;
import com.example.userservice.vo.UserResponse;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);

    List<UserResponse> getUserByAll();
}
