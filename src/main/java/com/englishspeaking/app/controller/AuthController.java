package com.englishspeaking.app.controller;

import com.englishspeaking.app.dto.request.LoginRequest;
import com.englishspeaking.app.dto.request.SignupRequest;
import com.englishspeaking.app.dto.response.ApiResponse;
import com.englishspeaking.app.dto.response.UserResponse;
import com.englishspeaking.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse userResponse = authService.registerUser(request);
        ApiResponse response = new ApiResponse(true, "User registered successfully", userResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        UserResponse userResponse = authService.authenticateUser(request);
        ApiResponse response = new ApiResponse(true, "Login successful", userResponse);
        return ResponseEntity.ok(response);
    }
}
