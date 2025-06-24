package com.parker.patientservice.service.impl;

import com.parker.patientservice.constants.PatientConstants;
import com.parker.patientservice.service.OtpSenderFactory;
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

    public void generateAndSendOtp(String channel, String destination) {
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        String key = getOtpKey(channel, destination);
        otpSenderRedisTemplate.opsForValue().set(key, otp, Long.parseLong(PatientConstants.OTP_TTL_SECONDS.getValue()), TimeUnit.SECONDS);
        senderFactory.getOtpSender(channel).sendOtp(destination, otp);
    }

    public boolean verifyOtp(String channel, String destination, String submittedOtp) {
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
        return PatientConstants.OTP.getValue() + ":" + channel + ":" + destination;
    }
}
