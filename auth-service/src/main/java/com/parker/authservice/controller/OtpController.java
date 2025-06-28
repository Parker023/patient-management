package com.parker.authservice.controller;

import com.parker.authservice.constants.AuthConstants;
import com.parker.authservice.dto.OtpRequestDto;
import com.parker.authservice.dto.VerifyOtpDto;
import com.parker.authservice.service.impl.OtpManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author shanmukhaanirudhtalluri
 * @date 25/06/25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patient/otp/")
@Tag(name = "Patient", description = "API for managing patients")
public class OtpController {
    private final OtpManager otpManager;

    @PostMapping("send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequestDto otpRequestDto) {
        otpManager.generateAndSendOtp(AuthConstants.EMAIL.getValue(), otpRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("verify")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpDto verifyOtpDto) {
        boolean isVerified = otpManager.verifyOtp(AuthConstants.EMAIL.getValue(), verifyOtpDto);
        String message = isVerified ? AuthConstants.OTP_VERIFIED.getValue() : AuthConstants.INVALID_OTP.getValue();
        return ResponseEntity.ok(message);
    }
}
