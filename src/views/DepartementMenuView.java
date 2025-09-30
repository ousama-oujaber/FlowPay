package src.views;

import src.controllers.DepartementController;
import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Departement;
import src.models.Paiement;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class DepartementMenuView extends BaseMenuView {

	private static final Logger VIEW_LOGGER = Logger.getLogger(DepartementMenuView.class.getName());
	private static final String DEPARTMENT_ID_PROMPT = "ID du département : ";
	private final DepartementController departementController;

	public DepartementMenuView(Scanner scanner,
							   DepartementController departementController) {
		super(scanner, VIEW_LOGGER);
		this.departementController = departementController;
	}

	public void start() {
		boolean running = true;
		while (running) {
			logSection("=== Gestion des départements ===");
			logInfo("1. Lister les départements");
			logInfo("2. Ajouter un département");
			logInfo("3. Modifier un département");
			logInfo("4. Supprimer un département");
			logInfo("5. Assigner un responsable");
			logInfo("6. Lister les agents d'un département");
			logInfo("7. Voir les paiements d'un département");
			logInfo("0. Retour");
			String choice = prompt("Choix : ");

			switch (choice) {
				case "1" -> listDepartements();
				case "2" -> createDepartement();
				case "3" -> updateDepartement();
				case "4" -> deleteDepartement();
				case "5" -> assignResponsable();
				case "6" -> listAgents();
				case "7" -> listPayments();
				case "0" -> running = false;
				default -> logInfo("Choix invalide");
			}
		}
	}

	private void listDepartements() {
		List<Departement> departements = departementController.listDepartements();
		if (departements.isEmpty()) {
			logInfo("Aucun département.");
			return;
		}
		logSection("--- Départements ---");
		for (Departement departement : departements) {
			String responsable = departement.getResponsable() != null
					? departement.getResponsable().getNomComplet()
					: "Aucun";
			logInfo(String.format("[%d] %s - Responsable : %s",
					departement.getIdDepartement(),
					departement.getNom(),
					responsable));
		}
	}

	private void createDepartement() {
		try {
			logSection("--- Nouveau département ---");
			String nom = prompt("Nom : ").trim();
			Integer responsableId = promptAgentIdOptional();
			Departement departement = departementController.createDepartement(nom, responsableId);
			logInfo("Département créé avec l'id " + departement.getIdDepartement());
		} catch (IllegalArgumentException | AgentNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private void updateDepartement() {
		try {
			int departementId = promptInt("ID du département à modifier : ");
			Departement departement = departementController.listDepartements()
					.stream()
					.filter(d -> d.getIdDepartement() == departementId)
					.findFirst()
					.orElseThrow(() -> new DepartementNotFoundException("Département introuvable"));

			String nom = prompt(String.format("Nom [%s] : ", departement.getNom())).trim();
			if (nom.isEmpty()) {
				nom = departement.getNom();
			}

			String value = prompt("ID responsable (laisser vide pour aucun) : ").trim();
			Integer responsableId = value.isEmpty() ? null : Integer.parseInt(value);

			departementController.updateDepartement(departementId, nom, responsableId);
			logInfo("Département mis à jour.");
		} catch (DepartementNotFoundException | AgentNotFoundException e) {
			logError(e.getMessage());
		} catch (NumberFormatException e) {
			logInfo("Valeur invalide.");
		}
	}

	private void deleteDepartement() {
		try {
			int departementId = promptInt("ID du département à supprimer : ");
			departementController.deleteDepartement(departementId);
			logInfo("Département supprimé.");
		} catch (DepartementNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private void assignResponsable() {
		try {
			int departementId = promptInt(DEPARTMENT_ID_PROMPT);
			int agentId = promptInt("ID de l'agent : ");
			departementController.assignResponsable(departementId, agentId);
			logInfo("Responsable assigné.");
		} catch (DepartementNotFoundException | AgentNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private void listAgents() {
		try {
			int departementId = promptInt(DEPARTMENT_ID_PROMPT);
			List<Agent> agents = departementController.listAgents(departementId);
			if (agents.isEmpty()) {
				logInfo("Aucun agent dans ce département.");
				return;
			}
			logSection("Agents :");
			for (Agent agent : agents) {
				logInfo(String.format("[%d] %s (%s)",
						agent.getIdAgent(),
						agent.getNomComplet(),
						agent.getTypeAgent()));
			}
		} catch (DepartementNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private void listPayments() {
		try {
			int departementId = promptInt(DEPARTMENT_ID_PROMPT);
			List<Paiement> paiements = departementController.listPayments(departementId);
			if (paiements.isEmpty()) {
				logInfo("Aucun paiement pour ce département.");
				return;
			}
			logSection("Paiements du département :");
			for (Paiement paiement : paiements) {
				logInfo(String.format("[%d] %s - %.2f - %s",
						paiement.getIdPaiement(),
						paiement.getType(),
						paiement.getMontant(),
						paiement.getDate()));
			}
		} catch (DepartementNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private Integer promptAgentIdOptional() {
		String value = prompt("ID responsable (laisser vide pour aucun) : ").trim();
		if (value.isEmpty()) {
			return null;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			logInfo("Valeur invalide. Aucun responsable assigné.");
			return null;
		}
	}


}
