package com.parker.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * @author shanmukhaanirudhtalluri
 * @date 25/06/25
 */
@Getter
public class VerifyOtpDto {
    @NotBlank(message = "OTP should not be empty")
    private String otp;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email should not be null")
    private String email;
}
