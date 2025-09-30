package src.views;

import src.controllers.AuthController;

import java.util.Scanner;
import java.util.logging.Logger;

public class MainMenuView extends BaseMenuView {

	private static final Logger VIEW_LOGGER = Logger.getLogger(MainMenuView.class.getName());

	private final AuthController authController;
	private final AgentMenuView agentMenuView;
	private final DepartementMenuView departementMenuView;
	private final PaiementMenuView paiementMenuView;
	private final StatisticsMenuView statisticsMenuView;

	public MainMenuView(Scanner scanner,
					   AuthController authController,
					   AgentMenuView agentMenuView,
					   DepartementMenuView departementMenuView,
					   PaiementMenuView paiementMenuView,
					   StatisticsMenuView statisticsMenuView) {
		super(scanner, VIEW_LOGGER);
		this.authController = authController;
		this.agentMenuView = agentMenuView;
		this.departementMenuView = departementMenuView;
		this.paiementMenuView = paiementMenuView;
		this.statisticsMenuView = statisticsMenuView;
	}

	public void start() {
		boolean running = true;
		while (running && authController.isAuthenticated()) {
			logSection("=== Menu principal ===");
			logInfo("1. Gestion des agents");
			logInfo("2. Gestion des départements");
			logInfo("3. Gestion des paiements");
			logInfo("4. Statistiques");
			logInfo("5. Voir la session");
			logInfo("0. Déconnexion");
			String choice = prompt("Choix : ");

			switch (choice) {
				case "1" -> agentMenuView.start();
				case "2" -> departementMenuView.start();
				case "3" -> paiementMenuView.start();
				case "4" -> statisticsMenuView.start();
				case "5" -> showSession();
				case "0" -> {
					logout();
					running = false;
				}
				default -> logInfo("Choix invalide");
			}
		}
		if (!authController.isAuthenticated()) {
			logInfo("Session terminée.");
		}
	}

	private void showSession() {
		authController.getCurrentAgent().ifPresentOrElse(agent -> {
			logSection("=== Session en cours ===");
			logInfo("Agent : " + agent.getNomComplet());
			logInfo("Type : " + agent.getTypeAgent());
			logInfo("Email : " + agent.getEmail());
		}, () -> logInfo("Aucune session active."));
	}

	private void logout() {
		if (authController.isAuthenticated()) {
			authController.logout();
			logInfo("Déconnexion réussie.");
		} else {
			logInfo("Aucune session à fermer.");
		}
	}
}
