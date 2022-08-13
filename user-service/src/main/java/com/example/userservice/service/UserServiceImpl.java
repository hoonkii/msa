package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.domain.UserRepository;
import com.example.userservice.dto.UserDto;
import com.example.userservice.vo.OrderResponse;
import com.example.userservice.vo.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User user = mapper.map(userDto, User.class);
        user.setEncryptedPwd(encoder.encode(userDto.getPwd()));
        userRepository.save(user);
        return null;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        Optional<User> user = userRepository.findById(userId);
        user.orElseThrow(() -> new UsernameNotFoundException("user not found"));


        UserDto userDto = new ModelMapper().map(user, UserDto.class);
        List<OrderResponse> orders = new ArrayList<>();
        userDto.setOrders(orders);
        return null;
    }

    @Override
    public List<UserResponse> getUserByAll() {
        Iterable<User> users = userRepository.findAll();

        List<UserResponse> result = new ArrayList<>();
        users.forEach(v -> {
            result.add(new ModelMapper().map(v, UserResponse.class));
        });

        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        username
                )
        );
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPwd(), true, true, true, true, new ArrayList<>());

    }
}
