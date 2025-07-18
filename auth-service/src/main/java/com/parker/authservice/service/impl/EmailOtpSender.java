package com.parker.authservice.service.impl;

import com.parker.authservice.constants.AuthConstants;
import com.parker.authservice.service.OtpSender;
import com.parker.authservice.util.EmailTemplateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
@Service("emailOtpService")
@Slf4j
@RequiredArgsConstructor
public class EmailOtpSender implements OtpSender {
    private final EmailTemplateProcessor emailTemplateProcessor;
    private final JavaMailSender mailSender;

    @Override
    public String getChannel() {
        return AuthConstants.EMAIL.getValue();
    }

    @Override
    public void sendOtp(String email, String otp) {
        String name = email.substring(0, email.indexOf("@"));
        log.info("Sending OTP to email: {}", email);

        Map<String, String> templateValueMap = new HashMap<>();
        templateValueMap.put("name", name);
        templateValueMap.put("otp", otp);

        String mailTemplate = emailTemplateProcessor
                .processTemplate(AuthConstants.EMAIL_TEMPLATE.getValue(), templateValueMap);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Patient OTP Verification");
        message.setText(mailTemplate);
        mailSender.send(message);

    }
}
