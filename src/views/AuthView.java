package src.views;

import src.controllers.AuthController;
import src.exceptions.AuthenticationException;
import src.models.Agent;

import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

public class AuthView extends BaseMenuView {
	private static final int MAX_ATTEMPTS = 3;
	private static final Logger VIEW_LOGGER = Logger.getLogger(AuthView.class.getName());

	private final AuthController authController;

	public AuthView(AuthController authController) {
		this(authController, new Scanner(System.in));
	}

	public AuthView(AuthController authController, Scanner scanner) {
		super(scanner, VIEW_LOGGER);
		this.authController = authController;
	}

	public Optional<Agent> showLogin() {
		int attempts = 0;

		while (attempts < MAX_ATTEMPTS && !authController.isAuthenticated()) {
			logSection("=== Authentification ===");
			String email = prompt("Email : ").trim();
			String password = prompt("Mot de passe : ").trim();

			try {
				Agent agent = authController.login(email, password);
				logInfo("Bienvenue " + agent.getPrenom() + " " + agent.getNom() + " !");
				return Optional.of(agent);
			} catch (AuthenticationException e) {
				attempts++;
				logError(e.getMessage());
				if (attempts < MAX_ATTEMPTS) {
					logInfo("Il vous reste " + (MAX_ATTEMPTS - attempts) + " tentative(s).");
				}
			}
		}

		logInfo("Nombre maximum de tentatives atteint.");
		return Optional.empty();
	}

	public void showCurrentSession() {
		authController.getCurrentAgent().ifPresent(agent -> {
			logSection("=== Session en cours ===");
			logInfo("Agent : " + agent.getNomComplet());
			logInfo("Type : " + agent.getTypeAgent());
			logInfo("Email : " + agent.getEmail());
		});
	}

	public void logout() {
		if (authController.isAuthenticated()) {
			authController.logout();
			logInfo("Déconnexion réussie.");
		} else {
			logInfo("Aucune session active.");
		}
	}
}
