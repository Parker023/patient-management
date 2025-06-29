package com.parker.authservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
}
