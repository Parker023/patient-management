package com.parker.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationRequest {
    @NotBlank(message = "name should not be empty")
    @Size(min = 5, max = 50, message = "name size should be 5>nameSize>50 ")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be a valid email address")
    private String email;
    @NotBlank(message = "Password is not valid")
    private String password;
    private String role;
    @NotBlank(message = "Date Of Birth should not be empty")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    private String gender;

    private String address;
}
