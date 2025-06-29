package com.parker.authservice.service.impl;

import com.parker.authservice.constants.AuthConstants;
import com.parker.authservice.dto.OtpRequestDto;
import com.parker.authservice.dto.PendingRegistration;
import com.parker.authservice.dto.RegistrationRequest;
import com.parker.authservice.dto.VerifyOtpDto;
import com.parker.authservice.service.OtpSenderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OtpManager {
    private final OtpSenderFactory senderFactory;
    private final RedisTemplate<String, PendingRegistration> otpSenderRedisTemplate;


    public void generateAndSendOtp(String channel, RegistrationRequest registrationRequest) {
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        log.info("Sending OTP {} to channel {}", otp, channel);
        String key = getOtpKey(channel, registrationRequest.getEmail());
        PendingRegistration registration = constructPendingRegistration(otp, registrationRequest);
        otpSenderRedisTemplate.opsForValue().set(key, registration, 3, TimeUnit.MINUTES);
        senderFactory.getOtpSender(channel).sendOtp(registrationRequest.getEmail(), otp);
        log.info("OTP sent successfully");
    }

    public boolean verifyOtp(String channel, VerifyOtpDto verifyOtpDto) {
        String destination = verifyOtpDto.getEmail();
        String submittedOtp = verifyOtpDto.getOtp();
        String key = getOtpKey(channel, destination);
        PendingRegistration pendingRegistration = otpSenderRedisTemplate.opsForValue().get(key);
        log.info("pending registration is {}", pendingRegistration);
        if (Objects.isNull(pendingRegistration)) {
            return false;
        }
        log.info("submitted OTP:{}", submittedOtp);
        return Objects.nonNull(pendingRegistration.getOtp()) && pendingRegistration.getOtp().equals(submittedOtp);
    }

    public String getOtpKey(String channel, String destination) {
        return AuthConstants.PENDING.getValue() + AuthConstants.COLON.getValue() + channel + AuthConstants.COLON.getValue() + destination;
    }

    private PendingRegistration constructPendingRegistration(String otp, RegistrationRequest request) {
        return PendingRegistration.builder()
                .registrationData(request)
                .otp(otp)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
