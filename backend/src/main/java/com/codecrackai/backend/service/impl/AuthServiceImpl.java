package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.request.ForgotPasswordRequest;
import com.codecrackai.backend.dto.request.LoginRequest;
import com.codecrackai.backend.dto.request.ResetPasswordRequest;
import com.codecrackai.backend.dto.request.SignupRequest;
import com.codecrackai.backend.dto.response.AuthResponse;
import com.codecrackai.backend.dto.response.UserProfileResponse;
import com.codecrackai.backend.entity.PasswordResetToken;
import com.codecrackai.backend.entity.User;
import com.codecrackai.backend.entity.enums.Role;
import com.codecrackai.backend.exception.BadRequestException;
import com.codecrackai.backend.exception.ResourceNotFoundException;
import com.codecrackai.backend.exception.UnauthorizedException;
import com.codecrackai.backend.mapper.UserMapper;
import com.codecrackai.backend.repository.PasswordResetTokenRepository;
import com.codecrackai.backend.repository.UserRepository;
import com.codecrackai.backend.security.JwtService;
import com.codecrackai.backend.service.AuthService;
import com.codecrackai.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final EmailService emailService;

    private static final long RESET_TOKEN_VALIDITY_MINUTES = 30;

    @Override
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with this email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.USER)
                .build();
        userRepository.save(user);

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        updateStreak(user);
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        if (!jwtService.isParsable(refreshToken) || !"refresh".equals(jwtService.extractTokenType(refreshToken))) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        String email = jwtService.extractEmail(refreshToken);
        if (!jwtService.isTokenValid(refreshToken, email)) {
            throw new UnauthorizedException("Refresh token expired or invalid");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User no longer exists"));
        return buildAuthResponse(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(UUID.randomUUID().toString())
                    .user(user)
                    .expiresAt(Instant.now().plusSeconds(RESET_TOKEN_VALIDITY_MINUTES * 60))
                    .build();
            resetTokenRepository.save(resetToken);

            String resetLink = "http://localhost:5173/reset-password?token=" + resetToken.getToken();
            emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        });
        // Deliberately no error when the email doesn't exist, so this endpoint
        // can't be used to enumerate registered accounts.
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        if (resetToken.isUsed() || resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("Invalid or expired reset token");
        }

        User user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        resetToken.setUsed(true);

        userRepository.save(user);
        resetTokenRepository.save(resetToken);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.of("User", userId));
        return userMapper.toProfile(user);
    }

    private void updateStreak(User user) {
        LocalDate today = LocalDate.now();
        LocalDate last = user.getLastActiveDate();

        if (last == null || last.isBefore(today.minusDays(1))) {
            user.setCurrentStreak(1);
        } else if (last.equals(today.minusDays(1))) {
            user.setCurrentStreak(user.getCurrentStreak() + 1);
        } // else last == today: already counted, no change

        user.setLongestStreak(Math.max(user.getLongestStreak(), user.getCurrentStreak()));
        user.setLastActiveDate(today);
        userRepository.save(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toProfile(user))
                .build();
    }
}
