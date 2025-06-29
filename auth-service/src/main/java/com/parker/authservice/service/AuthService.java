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
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<UserDto> validateOtp(VerifyOtpDto verifyOtpDto) {
        return CompletableFuture
                .supplyAsync(() -> otpManager
                        .verifyOtp(AuthConstants.EMAIL.getValue(), verifyOtpDto))
                .thenApplyAsync(isVerified -> {
                    if (!Boolean.TRUE.equals(isVerified)) {
                        throw new OtpVerificationException("Otp verification failed");
                    }
                    return otpManager.getOtpKey(AuthConstants.EMAIL.getValue(), verifyOtpDto.getEmail());
                })
                .thenApplyAsync(redisKey -> {
                    PendingRegistration pendingRegistration = otpSenderRedisTemplate.opsForValue().get(redisKey);
                    if (Objects.isNull(pendingRegistration)) {
                        throw new OtpVerificationException("OTP verification failed");
                    }
                    log.info("OTP verified. Deleting OTP from Redis.");
                    otpSenderRedisTemplate.delete(redisKey);
                    return pendingRegistration.getRegistrationData();
                })
                .thenApplyAsync(request -> {
                    request.setPassword(passwordEncoder.encode(request.getPassword()));

                    AuthRequest authRequest = entityDtoMapper.toDto(request, AuthRequest.class);
                    User user = entityDtoMapper.toEntity(authRequest, User.class);

                    user.setId(UuidCreator.getTimeBased());
                    if (Objects.isNull(user.getRole())) {
                        user.setRole("ROLE_USER");
                    }

                    User savedUser = userService.save(user);
                    CompletableFuture.runAsync(() -> kafkaProducer.sendToPatient(request));
                    return entityDtoMapper.toDto(savedUser, UserDto.class);
                });


    }
}
