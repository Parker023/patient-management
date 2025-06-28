package com.parker.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

/**
 * @author shanmukhaanirudhtalluri
 * @date 25/06/25
 */
@Builder
@Getter
public class OtpRequestDto {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email should not be null")
    private String email;
}
