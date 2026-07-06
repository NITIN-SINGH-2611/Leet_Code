package com.codecrackai.backend.service;

import com.codecrackai.backend.dto.request.ForgotPasswordRequest;
import com.codecrackai.backend.dto.request.LoginRequest;
import com.codecrackai.backend.dto.request.ResetPasswordRequest;
import com.codecrackai.backend.dto.request.SignupRequest;
import com.codecrackai.backend.dto.response.AuthResponse;
import com.codecrackai.backend.dto.response.UserProfileResponse;

public interface AuthService {
    AuthResponse signup(SignupRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(String refreshToken);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    UserProfileResponse getProfile(Long userId);
}
