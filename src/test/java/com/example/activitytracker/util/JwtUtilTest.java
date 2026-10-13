package com.example.activitytracker.util;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void shouldRejectMissingSecret() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new JwtUtil("", 86400000)
        );

        assertTrue(exception.getMessage().contains("not configured"));
    }

    @Test
    void shouldRejectInvalidBase64Secret() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new JwtUtil("not-valid-base64!!", 86400000)
        );

        assertTrue(exception.getMessage().contains("Base64"));
    }

    @Test
    void shouldRejectSecretShorterThan32Bytes() {
        String weakSecret = Base64.getEncoder()
                .encodeToString(new byte[16]);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new JwtUtil(weakSecret, 86400000)
        );

        assertTrue(exception.getMessage().contains("32 bytes"));
    }

    @Test
    void shouldAcceptValidSecret() {
        String validSecret = Base64.getEncoder()
                .encodeToString(new byte[32]);

        assertDoesNotThrow(
                () -> new JwtUtil(validSecret, 86400000)
        );
    }
}
