package com.nutriverify.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * SHA-256 password hashing. This is deliberately simple (no salt, no
 * PBKDF2) because the master prompt restricts us to Core Java and a
 * local-file demo project; it is explicitly NOT production-grade
 * cryptography. The abstraction makes it easy to swap in a stronger
 * scheme later.
 */
public final class PasswordHasher {
    private PasswordHasher() {}

    /**
     * Returns the SHA-256 hex digest of the given password.
     */
    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException ex) {
            // SHA-256 is mandated by the Java specification, so this is
            // effectively unreachable. Re-throw to fail loudly if it ever
            // happens rather than silently storing plaintext.
            throw new IllegalStateException("SHA-256 algorithm not available", ex);
        }
    }

    /**
     * Returns true if the plaintext password hashes to the stored digest.
     */
    public static boolean matches(String password, String storedHash) {
        return hash(password).equalsIgnoreCase(storedHash);
    }
}
