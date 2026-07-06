package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.request.LoginRequest;
import com.codecrackai.backend.dto.request.SignupRequest;
import com.codecrackai.backend.dto.response.AuthResponse;
import com.codecrackai.backend.dto.response.UserProfileResponse;
import com.codecrackai.backend.entity.User;
import com.codecrackai.backend.entity.enums.Role;
import com.codecrackai.backend.exception.BadRequestException;
import com.codecrackai.backend.exception.UnauthorizedException;
import com.codecrackai.backend.mapper.UserMapper;
import com.codecrackai.backend.repository.PasswordResetTokenRepository;
import com.codecrackai.backend.repository.UserRepository;
import com.codecrackai.backend.security.JwtService;
import com.codecrackai.backend.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordResetTokenRepository resetTokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;
    @Mock private EmailService emailService;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(userRepository, resetTokenRepository, passwordEncoder,
                authenticationManager, jwtService, userMapper, emailService);
    }

    @Test
    void signupRejectsDuplicateEmail() {
        SignupRequest request = new SignupRequest("Jane Doe", "jane@example.com", "password123");
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void signupCreatesUserAndReturnsTokens() {
        SignupRequest request = new SignupRequest("Jane Doe", "jane@example.com", "password123");
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(jwtService.generateAccessToken(anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");
        when(userMapper.toProfile(any(User.class)))
                .thenReturn(UserProfileResponse.builder().email("jane@example.com").build());

        AuthResponse response = authService.signup(request);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getUser().getEmail()).isEqualTo("jane@example.com");

        verify(userRepository).save(argThat(u ->
                u.getEmail().equals("jane@example.com")
                        && u.getPasswordHash().equals("hashed")
                        && u.getRole() == Role.USER));
    }

    @Test
    void loginThrowsUnauthorizedOnBadCredentials() {
        LoginRequest request = new LoginRequest("jane@example.com", "wrongpass", false);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad creds"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void loginSucceedsAndUpdatesStreakForFirstActivity() {
        LoginRequest request = new LoginRequest("jane@example.com", "password123", false);
        User user = User.builder().email("jane@example.com").passwordHash("hashed")
                .fullName("Jane").role(Role.USER).currentStreak(0).longestStreak(0).build();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");
        when(userMapper.toProfile(any(User.class)))
                .thenReturn(UserProfileResponse.builder().email("jane@example.com").build());

        AuthResponse response = authService.login(request);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(user.getCurrentStreak()).isEqualTo(1);
        verify(userRepository, atLeastOnce()).save(user);
    }
}
