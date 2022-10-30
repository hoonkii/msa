package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.UserRequest;
import com.example.userservice.vo.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final Environment environment;

    @GetMapping("/health_check")
    public String status() {
        return String.format("i am alive %s", environment.getProperty("local.server.port"));
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody UserRequest userRequest) {
        System.out.println("호출이 되긴했니?");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(userRequest, UserDto.class);
        userService.createUser(userDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> userList = userService.getUserByAll();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") String userId) {
        log.info("Before call order service");
        UserDto userDto = userService.getUserByUserId(userId);
        log.info("After call order service");
        UserResponse returnValue = new ModelMapper().map(userDto, UserResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
