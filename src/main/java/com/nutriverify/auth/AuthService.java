package com.nutriverify.auth;

import com.nutriverify.exception.NutriVerifyException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Application-level authentication operations: registration and login.
 * This is the single place that enforces business rules (duplicate
 * email, password match) before persisting via {@link UserRepository}.
 */
public class AuthService {
    private final UserRepository repository;

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers a new user. Returns the created {@link User} on success.
     * @throws NutriVerifyException if the email is already registered.
     */
    public User register(String name, String email, String password, String passwordConfirm) {
        if (name == null || name.isBlank()) {
            throw new NutriVerifyException("Name cannot be empty.");
        }
        if (email == null || email.isBlank()) {
            throw new NutriVerifyException("Email cannot be empty.");
        }
        if (password == null || password.isBlank()) {
            throw new NutriVerifyException("Password cannot be empty.");
        }
        if (!password.equals(passwordConfirm)) {
            throw new NutriVerifyException("Password and confirmation do not match.");
        }
        if (repository.findByEmail(email).isPresent()) {
            throw new NutriVerifyException("An account with that email already exists.");
        }
        User user = new User(
            name.trim(),
            email.trim(),
            PasswordHasher.hash(password),
            LocalDateTime.now());
        repository.save(user);
        return user;
    }

    /**
     * Attempts to log in with the given credentials.
     * @return a populated {@link AuthSession} on success.
     * @throws NutriVerifyException if the email is unknown or the password is wrong.
     */
    public AuthSession login(String email, String password) {
        Optional<User> found = repository.findByEmail(email);
        if (found.isEmpty()) {
            throw new NutriVerifyException("No account found for that email.");
        }
        User user = found.get();
        if (!PasswordHasher.matches(password, user.getPasswordHash())) {
            throw new NutriVerifyException("Incorrect password. Please try again.");
        }
        return new AuthSession(user, LocalDateTime.now());
    }
}
