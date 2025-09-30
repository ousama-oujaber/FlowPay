package src.views;

import src.controllers.AgentController;
import src.controllers.DepartementController;
import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Departement;
import src.models.Paiement;
import src.models.TypeAgent;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class AgentMenuView extends BaseMenuView {

	private static final Logger VIEW_LOGGER = Logger.getLogger(AgentMenuView.class.getName());
	private final AgentController agentController;
	private final DepartementController departementController;

	public AgentMenuView(Scanner scanner, AgentController agentController, DepartementController departementController) {
		super(scanner, VIEW_LOGGER);
		this.agentController = agentController;
		this.departementController = departementController;
	}

	public void start() {
		boolean running = true;
		while (running) {
			logSection("=== Gestion des agents ===");
			logInfo("1. Lister les agents");
			logInfo("2. Ajouter un agent");
			logInfo("3. Modifier un agent");
			logInfo("4. Supprimer un agent");
			logInfo("5. Voir les paiements d'un agent");
			logInfo("0. Retour");
			String choice = prompt("Choix : ");

			switch (choice) {
				case "1" -> listAgents();
				case "2" -> createAgent();
				case "3" -> updateAgent();
				case "4" -> deleteAgent();
				case "5" -> showAgentPayments();
				case "0" -> running = false;
				default -> logInfo("Choix invalide");
			}
		}
	}

	private void listAgents() {
		List<Agent> agents = agentController.listAgents();
		if (agents.isEmpty()) {
			logInfo("Aucun agent enregistré.");
			return;
		}
		logSection("--- Agents ---");
		for (Agent agent : agents) {
			String dept = agent.getDepartement() != null ? agent.getDepartement().getNom() : "Sans département";
			logInfo(String.format("[%d] %s %s - %s (%s)",
					agent.getIdAgent(),
					agent.getPrenom(),
					agent.getNom(),
					dept,
					agent.getTypeAgent()));
		}
	}

	private void createAgent() {
		try {
			logSection("--- Nouvel agent ---");
			String nom = prompt("Nom : ").trim();
			String prenom = prompt("Prénom : ").trim();
			String email = prompt("Email : ").trim();
			String motDePasse = prompt("Mot de passe : ").trim();
			TypeAgent typeAgent = promptTypeAgent();
			Integer departementId = promptDepartementId();

			Agent agent = agentController.createAgent(nom, prenom, email, motDePasse, typeAgent, departementId);
			logInfo("Agent créé avec l'id " + agent.getIdAgent());
		} catch (IllegalArgumentException | DepartementNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private void updateAgent() {
		try {
			int agentId = promptInt("ID de l'agent à modifier : ");
			Agent agent = agentController.getAgent(agentId);

			logInfo("Laisser vide pour conserver la valeur actuelle.");
			String nom = promptStringWithDefault("Nom", agent.getNom());
			String prenom = promptStringWithDefault("Prénom", agent.getPrenom());
			String email = promptStringWithDefault("Email", agent.getEmail());
			String motDePasse = promptStringWithDefault("Mot de passe", agent.getMotDePasse());
			TypeAgent type = promptTypeAgentWithDefault(agent.getTypeAgent());
			Integer departementId = promptDepartementIdWithDefault(agent.getDepartement());

			agentController.updateAgent(agentId, nom, prenom, email, motDePasse, type, departementId);
			logInfo("Agent mis à jour.");
		} catch (AgentNotFoundException | DepartementNotFoundException | IllegalArgumentException e) {
			logError(e.getMessage());
		}
	}

	private void deleteAgent() {
		try {
			int agentId = promptInt("ID de l'agent à supprimer : ");
			agentController.deleteAgent(agentId);
			logInfo("Agent supprimé.");
		} catch (AgentNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private void showAgentPayments() {
		try {
			int agentId = promptInt("ID de l'agent : ");
			List<Paiement> paiements = agentController.listPaymentsForAgent(agentId);
			double total = agentController.computeTotalPayments(agentId);
			if (paiements.isEmpty()) {
				logInfo("Aucun paiement pour cet agent.");
				return;
			}
			logSection("Paiements :");
			for (Paiement paiement : paiements) {
				logInfo(String.format("[%d] %s - %.2f (%s) - %s",
						paiement.getIdPaiement(),
						paiement.getType(),
						paiement.getMontant(),
						paiement.getDate(),
						paiement.getMotif()));
			}
			logInfo(String.format("Total : %.2f", total));
		} catch (AgentNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private String promptStringWithDefault(String label, String current) {
		String value = super.promptWithDefault(label, current).trim();
		return value.isEmpty() ? current : value;
	}

	private Integer promptDepartementId() {
		List<Departement> departements = departementController.listDepartements();
		if (departements.isEmpty()) {
			logInfo("Aucun département disponible. L'agent sera sans département.");
			return null;
		}
		logInfo("Départements disponibles :");
		for (Departement departement : departements) {
			logInfo(String.format("[%d] %s", departement.getIdDepartement(), departement.getNom()));
		}
		String value = prompt("ID du département (laisser vide pour aucun) : ").trim();
		if (value.isEmpty()) {
			return null;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			logInfo("Entrez un nombre valide. Aucun département assigné.");
			return null;
		}
	}

	private Integer promptDepartementIdWithDefault(Departement departement) {
		String current = departement != null ? String.valueOf(departement.getIdDepartement()) : "aucun";
		String value = prompt(String.format("ID du département [%s] (0 pour aucun) : ", current)).trim();
		if (value.isEmpty()) {
			return departement != null ? departement.getIdDepartement() : null;
		}
		if ("0".equals(value)) {
			return null;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			logInfo("Valeur invalide. Département inchangé.");
			return departement != null ? departement.getIdDepartement() : null;
		}
	}

	private TypeAgent promptTypeAgent() {
		logInfo("Types d'agent disponibles :");
		Arrays.stream(TypeAgent.values()).forEach(type -> logInfo("- " + type));
		while (true) {
			String value = prompt("Type : ").trim().toUpperCase();
			try {
				return TypeAgent.valueOf(value);
			} catch (IllegalArgumentException e) {
				logInfo("Type invalide. Réessayez.");
			}
		}
	}

	private TypeAgent promptTypeAgentWithDefault(TypeAgent current) {
		String value = prompt(String.format("Type [%s] : ", current)).trim();
		if (value.isEmpty()) {
			return current;
		}
		try {
			return TypeAgent.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			logInfo("Type invalide. Valeur conservée.");
			return current;
		}
	}
}
