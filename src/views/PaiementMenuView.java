package src.views;

import src.controllers.AgentController;
import src.controllers.PaiementController;
import src.exceptions.AgentNotFoundException;
import src.exceptions.InvalidPaymentConditionException;
import src.exceptions.NegativeAmountException;
import src.exceptions.PaiementNotFoundException;
import src.models.Agent;
import src.models.Paiement;
import src.models.TypePaiement;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class PaiementMenuView extends BaseMenuView {

	private final PaiementController paiementController;
	private final AgentController agentController;

	public PaiementMenuView(Scanner scanner,
							PaiementController paiementController,
							AgentController agentController) {
		super(scanner, Logger.getLogger(PaiementMenuView.class.getName()));
		this.paiementController = paiementController;
		this.agentController = agentController;
	}

	public void start() {
		boolean running = true;
		while (running) {
			logSection("=== Gestion des paiements ===");
			logInfo("1. Lister tous les paiements");
			logInfo("2. Ajouter un paiement");
			logInfo("3. Modifier un paiement");
			logInfo("4. Supprimer un paiement");
			logInfo("5. Voir les paiements d'un agent");
			logInfo("6. Filtrer par type");
			logInfo("7. Filtrer par période");
			logInfo("0. Retour");
			String choice = prompt("Choix : ");

			switch (choice) {
				case "1" -> listPaiements();
				case "2" -> createPaiement();
				case "3" -> updatePaiement();
				case "4" -> deletePaiement();
				case "5" -> listPaiementsByAgent();
				case "6" -> listPaiementsByType();
				case "7" -> listPaiementsByPeriod();
				case "0" -> running = false;
				default -> logInfo("Choix invalide");
			}
		}
	}

	private void listPaiements() {
		List<Paiement> paiements = paiementController.listPaiements();
		if (paiements.isEmpty()) {
			logInfo("Aucun paiement enregistré.");
			return;
		}
		paiements.forEach(this::printPaiement);
	}

	private void createPaiement() {
		try {
			int agentId = promptInt("ID de l'agent : ");
			Agent agent = agentController.getAgent(agentId);
			TypePaiement type = promptTypePaiement();
			double montant = promptDouble("Montant : ");
			String motif = prompt("Motif : ").trim();
			boolean condition = promptBoolean("Condition validée (o/n) : ");
			LocalDate date = promptDateOptional("Date (yyyy-MM-dd, vide pour aujourd'hui) : ");

			Paiement paiement = paiementController.createPaiement(agent.getIdAgent(), type, montant, motif, condition, date);
			logInfo("Paiement créé avec l'id " + paiement.getIdPaiement());
		} catch (AgentNotFoundException | NegativeAmountException | InvalidPaymentConditionException e) {
			logError(e.getMessage());
		}
	}

	private void updatePaiement() {
		try {
			int paiementId = promptInt("ID du paiement : ");
			Paiement paiement = paiementController.getPaiement(paiementId);

			TypePaiement type = promptTypePaiementWithDefault(paiement.getType());
			double montant = promptDoubleWithDefault("Montant", paiement.getMontant());
			String motif = promptWithDefault("Motif", paiement.getMotif()).trim();
			if (motif.isEmpty()) {
				motif = paiement.getMotif();
			}
			boolean condition = promptBooleanWithDefault("Condition validée (o/n)", paiement.isConditionValidee());
			LocalDate date = promptDateOptionalWithDefault("Date (yyyy-MM-dd)", paiement.getDate());

			paiementController.updatePaiement(paiementId, type, montant, motif, condition, date);
			logInfo("Paiement mis à jour.");
		} catch (PaiementNotFoundException | AgentNotFoundException |
				 NegativeAmountException | InvalidPaymentConditionException e) {
			logError(e.getMessage());
		}
	}

	private void deletePaiement() {
		try {
			int paiementId = promptInt("ID du paiement : ");
			paiementController.deletePaiement(paiementId);
			logInfo("Paiement supprimé.");
		} catch (PaiementNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private void listPaiementsByAgent() {
		try {
			int agentId = promptInt("ID de l'agent : ");
			List<Paiement> paiements = paiementController.listPaiementsForAgent(agentId);
			if (paiements.isEmpty()) {
				logInfo("Aucun paiement pour cet agent.");
				return;
			}
			paiements.forEach(this::printPaiement);
			double total = paiementController.computeTotalByAgent(agentId);
			double average = paiementController.computeAverageByAgent(agentId);
			logInfo(String.format("Total : %.2f | Moyenne : %.2f", total, average));
		} catch (AgentNotFoundException e) {
			logError(e.getMessage());
		}
	}

	private void listPaiementsByType() {
		TypePaiement type = promptTypePaiement();
		List<Paiement> paiements = paiementController.listPaiementsByType(type);
		if (paiements.isEmpty()) {
			logInfo("Aucun paiement de ce type.");
			return;
		}
		paiements.forEach(this::printPaiement);
	}

	private void listPaiementsByPeriod() {
		try {
			LocalDate start = promptDate("Date de début (yyyy-MM-dd) : ");
			LocalDate end = promptDate("Date de fin (yyyy-MM-dd) : ");
			List<Paiement> paiements = paiementController.listPaiementsByDateRange(start, end);
			if (paiements.isEmpty()) {
				logInfo("Aucun paiement sur cette période.");
				return;
			}
			paiements.forEach(this::printPaiement);
		} catch (IllegalArgumentException e) {
			logError(e.getMessage());
		}
	}

	private void printPaiement(Paiement paiement) {
		logInfo(String.format("[%d] Agent %d - %s - %.2f - %s (%s)",
				paiement.getIdPaiement(),
				paiement.getAgentId(),
				paiement.getType(),
				paiement.getMontant(),
				paiement.getDate(),
				paiement.getMotif()));
	}

	private TypePaiement promptTypePaiement() {
		logInfo("Types disponibles :");
		Arrays.stream(TypePaiement.values()).forEach(type -> logInfo("- " + type));
		while (true) {
			String input = prompt("Type : ").trim().toUpperCase();
			try {
				return TypePaiement.valueOf(input);
			} catch (IllegalArgumentException e) {
				logInfo("Type invalide.");
			}
		}
	}

	private TypePaiement promptTypePaiementWithDefault(TypePaiement current) {
		while (true) {
			String input = promptWithDefault("Type", current.toString()).trim();
			if (input.isEmpty()) {
				return current;
			}
			try {
				return TypePaiement.valueOf(input.toUpperCase());
			} catch (IllegalArgumentException e) {
				logInfo("Type invalide.");
			}
		}
	}
}
