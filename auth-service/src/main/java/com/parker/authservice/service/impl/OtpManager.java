package com.parker.authservice.service.impl;

import com.parker.authservice.constants.AuthConstants;
import com.parker.authservice.dto.OtpRequestDto;
import com.parker.authservice.dto.VerifyOtpDto;
import com.parker.authservice.service.OtpSenderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    private final RedisTemplate<String, String> otpSenderRedisTemplate;

    public void generateAndSendOtp(String channel, OtpRequestDto otpRequestDto) {
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        log.info("Sending OTP {} to channel {}", otp, channel);
        String key = getOtpKey(channel, otpRequestDto.getEmail());
        otpSenderRedisTemplate.opsForValue().set(key, otp, Long.parseLong(AuthConstants.OTP_TTL_SECONDS.getValue()), TimeUnit.SECONDS);
        senderFactory.getOtpSender(channel).sendOtp(otpRequestDto.getEmail(), otp);
    }

    public boolean verifyOtp(String channel, VerifyOtpDto verifyOtpDto) {
        String destination = verifyOtpDto.getEmail();
        String submittedOtp = verifyOtpDto.getOtp();
        String key = getOtpKey(channel, destination);
        String storedOtp = otpSenderRedisTemplate.opsForValue().get(key);
        log.info("submittedOtp:{}", submittedOtp);
        if (Objects.nonNull(storedOtp) && storedOtp.equals(submittedOtp)) {
            otpSenderRedisTemplate.delete(key);
            return true;
        }
        return false;
    }

    private String getOtpKey(String channel, String destination) {
        return AuthConstants.OTP.getValue() + ":" + channel + ":" + destination;
    }
}
