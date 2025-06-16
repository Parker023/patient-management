package com.parker.authservice.controller;

import com.parker.authservice.dto.*;
import com.parker.authservice.model.User;
import com.parker.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequestDTO) {

        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        // Authorization: Bearer <token>
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Register an User")
    @PostMapping("/register")
    public ResponseEntity<EntityModel<RegistrationResponse<UserDto>>> register(
            @RequestBody RegistrationRequest registrationRequest) {
        UserDto savedUser = authService.registerUser(registrationRequest);
        /*Link link=WebMvcLinkBuilder.linkTo(AuthController.class)
                .slash("login")
                .withRel("next")
                .withType("POST");*/
        RegistrationResponse<UserDto> registrationResponse = new RegistrationResponse<>(savedUser, "User registered successfully , Please login again !!!");
        EntityModel<RegistrationResponse<UserDto>> model=EntityModel.of(registrationResponse,
                linkTo(methodOn(AuthController.class).register(registrationRequest)).withSelfRel(),
                linkTo(methodOn(AuthController.class).login(null)).withRel("login")
        );
        return ResponseEntity.ok(model);
    }

}