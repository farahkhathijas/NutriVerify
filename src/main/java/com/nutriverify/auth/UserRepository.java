package com.nutriverify.auth;

import com.nutriverify.exception.NutriVerifyException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * File-backed repository for {@link User} accounts. The store is a simple
 * pipe-delimited text file at {@code AppConfig.USERS_PATH}; each line is:
 * <pre>name|email|passwordHash|createdAtIso</pre>
 * The file is created on first write if it does not exist.
 */
public class UserRepository {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Path path;

    public UserRepository(Path path) {
        this.path = path;
    }

    /**
     * Loads all users from disk. Returns an empty list if the file does
     * not yet exist (first run).
     */
    public List<User> findAll() {
        if (!Files.exists(path)) {
            return List.of();
        }
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                User user = parse(line);
                if (user != null) users.add(user);
            }
        } catch (IOException ex) {
            throw new NutriVerifyException("Unable to read user store: " + ex.getMessage(), ex);
        }
        return users;
    }

    /**
     * Finds a user by email (case-insensitive).
     */
    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return findAll().stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst();
    }

    /**
     * Appends a single user to the store, creating the file and parent
     * directories as needed. Duplicate-email detection is the caller's
     * responsibility (see {@link AuthService}).
     */
    public void save(User user) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            String line = serialize(user);
            try (BufferedWriter writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new NutriVerifyException("Unable to write user store: " + ex.getMessage(), ex);
        }
    }

    private String serialize(User user) {
        return escape(user.getName()) + "|"
            + escape(user.getEmail()) + "|"
            + user.getPasswordHash() + "|"
            + user.getCreatedAt().format(ISO);
    }

    private User parse(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 4) return null;
        try {
            return new User(
                unescape(parts[0]),
                unescape(parts[1]),
                parts[2],
                LocalDateTime.parse(parts[3], ISO));
        } catch (Exception ex) {
            return null;
        }
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("|", "/");
    }

    private static String unescape(String value) {
        return value == null ? "" : value;
    }
}
