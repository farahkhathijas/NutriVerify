package com.nutriverify.auth;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A registered NutriVerify user account. Stored as a single line in the
 * users file using pipe ({@code |}) as the field delimiter to avoid
 * collisions with commas in names.
 */
public final class User {
    private final String name;
    private final String email;
    private final String passwordHash;
    private final LocalDateTime createdAt;

    public User(String name, String email, String passwordHash, LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return email.equalsIgnoreCase(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email.toLowerCase());
    }

    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
}
