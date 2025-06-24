package com.parker.patientservice.service.impl;

import com.parker.patientservice.constants.PatientConstants;
import com.parker.patientservice.service.OtpSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
@Service("emailOtpService")
@Slf4j
public class EmailOtpSender implements OtpSender {
    @Override
    public String getChannel() {
        return PatientConstants.EMAIL.getValue();
    }

    @Override
    public void sendOtp(String destination, String otp) {
        log.info("Sending OTP to email: {}", otp);
    }
}
