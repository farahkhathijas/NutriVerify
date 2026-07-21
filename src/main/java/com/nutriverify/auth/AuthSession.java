package com.nutriverify.auth;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents the currently logged-in user session. Carried through the
 * application so downstream modules (history, profile) can attribute
 * activity to a specific account without re-prompting.
 */
public final class AuthSession {
    private final User user;
    private final LocalDateTime loginAt;

    public AuthSession(User user, LocalDateTime loginAt) {
        this.user = Objects.requireNonNull(user);
        this.loginAt = Objects.requireNonNull(loginAt);
    }

    public User getUser() { return user; }
    public LocalDateTime getLoginAt() { return loginAt; }

    public String displayName() {
        return user.getName();
    }
}
