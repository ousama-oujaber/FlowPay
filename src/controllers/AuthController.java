package src.controllers;

import src.exceptions.AuthenticationException;
import src.models.Agent;
import src.services.AuthService;

import java.util.Optional;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public Agent login(String username, String password) throws AuthenticationException {
        return authService.login(username, password);
    }

    public void logout() {
        authService.logout();
    }

    public boolean isAuthenticated() {
        return authService.isAuthenticated();
    }

    public Optional<Agent> getCurrentAgent() {
        return authService.getCurrentAgent();
    }
}
