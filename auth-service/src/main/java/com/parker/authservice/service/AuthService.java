package com.parker.authservice.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.parker.authservice.dto.LoginRequest;
import com.parker.authservice.dto.RegistrationRequest;
import com.parker.authservice.mapper.EntityDtoMapper;
import com.parker.authservice.model.User;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EntityDtoMapper entityDtoMapper;

    public Optional<String> authenticate(LoginRequest loginRequest) {
        return userService.loadUserByUsername(loginRequest.getEmail())
                .filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .map(user -> jwtService.generateToken(loginRequest, user.getRole()));

    }

    public boolean validateToken(String token) {
        try {
            jwtService.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }

    }

    public String registerUser(RegistrationRequest registrationRequest) {
        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        User user = entityDtoMapper.toEntity(registrationRequest, User.class);
        user.setId(UuidCreator.getTimeBased());
        if (Objects.isNull(user.getRole())) {
            user.setRole("ROLE_USER");
        }
        User savedUser = userService.save(user);
        return jwtService.generateToken(new LoginRequest(savedUser.getEmail(), savedUser.getPassword()), user.getRole());
    }
}
