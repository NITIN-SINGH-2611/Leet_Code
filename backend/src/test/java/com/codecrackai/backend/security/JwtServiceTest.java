package com.codecrackai.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        // 1 hour access, 1 day refresh, short dev-style secret (JwtService pads it internally)
        jwtService = new JwtService("test-secret-key-for-unit-tests", 3_600_000L, 86_400_000L);
    }

    @Test
    void generatesAccessTokenThatIsValidForItsOwnSubject() {
        String token = jwtService.generateAccessToken("user@example.com");

        assertThat(jwtService.isParsable(token)).isTrue();
        assertThat(jwtService.extractEmail(token)).isEqualTo("user@example.com");
        assertThat(jwtService.extractTokenType(token)).isEqualTo("access");
        assertThat(jwtService.isTokenValid(token, "user@example.com")).isTrue();
    }

    @Test
    void rejectsTokenValidationForAMismatchedEmail() {
        String token = jwtService.generateAccessToken("user@example.com");

        assertThat(jwtService.isTokenValid(token, "someone-else@example.com")).isFalse();
    }

    @Test
    void refreshTokenIsTaggedWithRefreshType() {
        String token = jwtService.generateRefreshToken("user@example.com");

        assertThat(jwtService.extractTokenType(token)).isEqualTo("refresh");
    }

    @Test
    void garbageInputIsNotParsable() {
        assertThat(jwtService.isParsable("not-a-real-jwt")).isFalse();
    }
}
