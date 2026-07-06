package com.codecrackai.backend.controller;

import com.codecrackai.backend.dto.request.ForgotPasswordRequest;
import com.codecrackai.backend.dto.request.LoginRequest;
import com.codecrackai.backend.dto.request.RefreshTokenRequest;
import com.codecrackai.backend.dto.request.ResetPasswordRequest;
import com.codecrackai.backend.dto.request.SignupRequest;
import com.codecrackai.backend.dto.response.ApiResponse;
import com.codecrackai.backend.dto.response.AuthResponse;
import com.codecrackai.backend.dto.response.UserProfileResponse;
import com.codecrackai.backend.security.CurrentUser;
import com.codecrackai.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ApiResponse.success("Account created", authService.signup(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.success(authService.refresh(request.getRefreshToken()));
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ApiResponse.success("If that email exists, a reset link has been sent", null);
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.success("Password updated successfully", null);
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> me(@CurrentUser Long userId) {
        return ApiResponse.success(authService.getProfile(userId));
    }
}
