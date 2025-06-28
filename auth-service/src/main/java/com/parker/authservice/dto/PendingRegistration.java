package com.parker.authservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author shanmukhaanirudhtalluri
 * @date 28/06/25
 */
@Data
@Builder
public class PendingRegistration {
    private String otp;
    private RegistrationRequest registrationData;
    private LocalDateTime createdAt;
}
