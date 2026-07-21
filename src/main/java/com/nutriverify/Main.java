package com.nutriverify;

import com.nutriverify.app.NutriVerifyApp;
import com.nutriverify.auth.AuthController;
import com.nutriverify.auth.AuthService;
import com.nutriverify.auth.AuthSession;
import com.nutriverify.auth.UserRepository;
import com.nutriverify.cli.Banner;
import com.nutriverify.cli.Console;
import com.nutriverify.config.AppConfig;

import java.nio.file.Path;
import java.util.Scanner;

/**
 * Entry point for the NutriVerify console application.
 * Boots the banner, runs the authentication gate, and only then
 * launches the authenticated application loop.
 */
public class Main {
    public static void main(String[] args) {
        Banner.print();
        Banner.printBootSequence();

        Scanner scanner = new Scanner(System.in);
        Console console = new Console(scanner);
        UserRepository userRepository = new UserRepository(Path.of(AppConfig.USERS_PATH));
        AuthService authService = new AuthService(userRepository);
        AuthController authController = new AuthController(console, authService);

        AuthSession session = authController.run();
        if (session == null) {
            return;
        }

        new NutriVerifyApp(console, scanner, session).run();
    }
}
