package com.paypal.user_service.controller;

import com.paypal.user_service.dto.LoginRequest;
import com.paypal.user_service.dto.LoginResponse;
import com.paypal.user_service.dto.SignUpRequest;
import com.paypal.user_service.dto.SignUpResponse;
import com.paypal.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Operations related to users")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "Sign up a new user", description = "Creates a new user and wallet")
    public ResponseEntity<SignUpResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }


    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity
                .ok(userService.loginUser(request));
    }

}