package com.chatkon.backend.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
    private static final SecureRandom Random = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        Random.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
