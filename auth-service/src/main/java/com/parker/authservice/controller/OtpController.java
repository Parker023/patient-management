package com.parker.authservice.controller;

import com.parker.authservice.dto.RegistrationResponse;
import com.parker.authservice.dto.UserDto;
import com.parker.authservice.dto.VerifyOtpDto;
import com.parker.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * @author shanmukhaanirudhtalluri
 * @date 25/06/25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patient/otp/")
@Tag(name = "Patient", description = "API for managing patients")
public class OtpController {
    private final AuthService authService;

    @PostMapping("verify")
    public CompletableFuture<ResponseEntity<EntityModel<RegistrationResponse<UserDto>>>> verifyOtp(@Valid @RequestBody VerifyOtpDto verifyOtpDto) {
        return authService.validateOtp(verifyOtpDto)
                .thenApply(user -> {
                    RegistrationResponse<UserDto> registrationResponse = new RegistrationResponse<>(user, "User registered successfully , Please login !!!");
                    EntityModel<RegistrationResponse<UserDto>> model = EntityModel.of(registrationResponse,
                            linkTo(methodOn(AuthController.class).register(null)).withSelfRel(),
                            linkTo(methodOn(AuthController.class).login(null)).withRel("login"));
                    return ResponseEntity.ok(model);
                });


    }
}
