package com.codecrackai.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the CodeCrack AI backend.
 * <p>
 * CodeCrack AI is an interview-preparation platform that curates the
 * highest-signal coding interview questions across major product companies
 * and delivers them through a smart, spaced-repetition study planner.
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
