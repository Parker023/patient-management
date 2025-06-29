package com.parker.authservice.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.parker.authservice.constants.AuthConstants;
import com.parker.authservice.dto.*;
import com.parker.authservice.exception.OtpVerificationException;
import com.parker.authservice.kafka.KafkaProducer;
import com.parker.authservice.mapper.EntityDtoMapper;
import com.parker.authservice.model.User;
import com.parker.authservice.service.impl.OtpManager;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EntityDtoMapper entityDtoMapper;
    private final OtpManager otpManager;
    private final KafkaProducer kafkaProducer;
    private final RedisTemplate<String, PendingRegistration> otpSenderRedisTemplate;

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

    public void registerUser(RegistrationRequest registrationRequest) {
        log.info("Register User");
        otpManager.generateAndSendOtp(AuthConstants.EMAIL.getValue(), registrationRequest);
    }

    public UserDto validateOtp(VerifyOtpDto verifyOtpDto) {
        boolean isVerified = otpManager.verifyOtp(AuthConstants.EMAIL.getValue(), verifyOtpDto);
        if (!isVerified) {
            throw new OtpVerificationException("Invalid OTP");
        }

        String key = otpManager.getOtpKey(AuthConstants.EMAIL.getValue(), verifyOtpDto.getOtp());
        PendingRegistration pendingRegistration = otpSenderRedisTemplate.opsForValue().get(key);

        if (Objects.isNull(pendingRegistration)) {
            throw new OtpVerificationException("OTP verification failed");
        }

        log.info("OTP verified. Deleting OTP from Redis.");
        otpSenderRedisTemplate.delete(key);

        RegistrationRequest registrationRequest = pendingRegistration.getRegistrationData();
        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        AuthRequest authRequest = entityDtoMapper.toDto(registrationRequest, AuthRequest.class);
        User user = entityDtoMapper.toEntity(authRequest, User.class);

        user.setId(UuidCreator.getTimeBased());
        if (Objects.isNull(user.getRole())) {
            user.setRole("ROLE_USER");
        }

        User savedUser = userService.save(user);

        kafkaProducer.sendToPatient(registrationRequest);

        return entityDtoMapper.toDto(savedUser, UserDto.class);

    }
}
