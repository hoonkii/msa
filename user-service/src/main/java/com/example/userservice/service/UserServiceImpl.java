package com.example.userservice.service;

import com.example.userservice.domain.UserEntity;
import com.example.userservice.domain.UserRepository;
import com.example.userservice.dto.UserDto;
import com.example.userservice.vo.OrderResponse;
import com.example.userservice.vo.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    private final Environment environment;

    private final RestTemplate restTemplate;

    private final OrderServiceClient orderServiceClient;

    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity user = mapper.map(userDto, UserEntity.class);
        user.setEncryptedPwd(encoder.encode(userDto.getPwd()));
        userRepository.save(user);
        return null;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        Optional<UserEntity> user = userRepository.findByUserId(userId);
        user.orElseThrow(() -> new RuntimeException("user not found"));


        UserDto userDto = new ModelMapper().map(user.get(), UserDto.class);

//        List<OrderResponse> ordersList = orderServiceClient.getOrders(userId);
        log.info("Before call orderservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker"); // 서킷 브래이커 생성
        List<OrderResponse> ordersList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId), throwable -> {
            log.info(throwable.toString());
            return new ArrayList<>();
        }); // 에러 시 처리
        log.info("After call orderservice");
        userDto.setOrders(ordersList);
        return userDto;
    }

    @Override
    public List<UserResponse> getUserByAll() {
        Iterable<UserEntity> users = userRepository.findAll();

        List<UserResponse> result = new ArrayList<>();
        users.forEach(v -> {
            result.add(new ModelMapper().map(v, UserResponse.class));
        });

        return result;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(username).orElseThrow(
//                () -> new UsernameNotFoundException(
//                        username
//                )
//        );
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
//
//    }
}
