package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("dev")
public class LoggingEmailService implements EmailService {

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        log.info("=== [DEV] Password reset email for {} ===\nReset link: {}\n=======================================",
                toEmail, resetLink);
    }
}
