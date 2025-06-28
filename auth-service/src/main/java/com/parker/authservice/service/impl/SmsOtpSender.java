package com.parker.authservice.service.impl;

import com.parker.authservice.constants.AuthConstants;
import com.parker.authservice.service.OtpSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
@Service("smsOtpService")
@Slf4j
public class SmsOtpSender implements OtpSender {
    @Override
    public String getChannel() {
        return AuthConstants.SMS.getValue();
    }

    @Override
    public void sendOtp(String destination, String otp) {
        log.info("Sending OTP to email: {}", otp);
    }
}
