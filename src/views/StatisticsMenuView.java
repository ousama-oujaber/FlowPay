package src.views;

import src.controllers.StatisticsController;
import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Paiement;
import src.models.TypePaiement;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class StatisticsMenuView extends BaseMenuView {

    private static final Logger VIEW_LOGGER = Logger.getLogger(StatisticsMenuView.class.getName());
    private static final String AGENT_ID_PROMPT = "ID de l'agent : ";
    private static final String DEPARTMENT_ID_PROMPT = "ID du département : ";

    private final StatisticsController statisticsController;

    public StatisticsMenuView(Scanner scanner, StatisticsController statisticsController) {
        super(scanner, VIEW_LOGGER);
        this.statisticsController = statisticsController;
    }

    public void start() {
        boolean running = true;
        while (running) {
            //show stats menu
            logSection("=== Statistiques ===");
            logInfo("1. Vue d'ensemble");
            logInfo("2. Total annuel d'un agent");
            logInfo("3. Nombre de paiements par type pour un agent");
            logInfo("4. Paiement le plus élevé pour un agent");
            logInfo("5. Totaux d'un département");
            logInfo("6. Classement des agents");
            logInfo("7. Distribution des paiements par type");
            logInfo("8. Paiements entre deux dates");
            logInfo("9. Détection paiement inhabituel");
            logInfo("0. Retour");
            String choice = prompt("Choix : ");

            switch (choice) {
                case "1" -> showOverview();
                case "2" -> showAnnualTotalForAgent();
                case "3" -> showPaymentsCountByType();
                case "4" -> showHighestPayment();
                case "5" -> showDepartmentTotals();
                case "6" -> listAgentRanking();
                case "7" -> showPaymentDistribution();
                case "8" -> listPaymentsBetween();
                case "9" -> detectUnusualPayment();
                case "0" -> running = false;
                default -> logInfo("Choix invalide");
            }
        }
    }

    private void showOverview() {
        double total = statisticsController.globalPaymentsTotal();
        long agents = statisticsController.totalAgents();
        long departements = statisticsController.totalDepartements();
        logSection("--- Vue d'ensemble ---");
        logInfo(String.format("Total global des paiements : %.2f", total));
        logInfo("Nombre d'agents : " + agents);
        logInfo("Nombre de départements : " + departements);
        showPaymentDistribution();
    }

    private void showAnnualTotalForAgent() {
        try {
            int agentId = promptInt(AGENT_ID_PROMPT);
            int year = promptInt("Year : ");
            double total = statisticsController.getAnnualTotalForAgent(agentId, year);
            logInfo(String.format("Total pour %s en %d : %.2f", agentId, year, total));
        } catch (AgentNotFoundException e) {
            logError(e.getMessage());
        }
    }

    private void showPaymentsCountByType() {
        try {
            int agentId = promptInt(AGENT_ID_PROMPT);
            TypePaiement type = promptTypePaiement();
            long count = statisticsController.countPaymentsByTypeForAgent(agentId, type);
            logInfo(String.format("%d paiement(s) de type %s", count, type));
        } catch (AgentNotFoundException e) {
            logError(e.getMessage());
        }
    }

    private void showHighestPayment() {
        try {
            int agentId = promptInt(AGENT_ID_PROMPT);
            statisticsController.highestPaymentForAgent(agentId)
                    .ifPresentOrElse(
                            paiement -> logInfo(String.format("Paiement max : %.2f le %s (%s)",
                                    paiement.getMontant(), paiement.getDate(), paiement.getMotif())),
                            () -> logInfo("Aucun paiement enregistré."));
        } catch (AgentNotFoundException e) {
            logError(e.getMessage());
        }
    }

    private void showDepartmentTotals() {
        try {
            int departementId = promptInt(DEPARTMENT_ID_PROMPT);
            double total = statisticsController.departmentTotal(departementId);
            double moyenneSalaire = statisticsController.departmentAverageSalary(departementId);
            logInfo(String.format("Total département : %.2f", total));
            logInfo(String.format("Salaire moyen : %.2f", moyenneSalaire));
        } catch (DepartementNotFoundException e) {
            logError(e.getMessage());
        }
    }

    private void listAgentRanking() {
        List<Agent> agents = statisticsController.rankAgentsByTotalPayments();
        if (agents.isEmpty()) {
            logInfo("Aucun agent enregistré.");
            return;
        }
        logSection("Classement des agents (total décroissant)");
        int position = 1;
        for (Agent agent : agents) {
            logInfo(String.format("%d. %s (%s)", position++, agent.getNomComplet(), agent.getTypeAgent()));
        }
    }

    private void showPaymentDistribution() {
        Map<TypePaiement, Long> distribution = statisticsController.paymentDistribution();
        if (distribution.isEmpty()) {
            logInfo("Aucun paiement enregistré.");
            return;
        }
        logSection("Distribution par type");
        distribution.forEach((type, count) -> logInfo(String.format("%s : %d", type, count)));
    }

    private void listPaymentsBetween() {
        LocalDate start = promptDate("Date de début (yyyy-MM-dd) : ");
        LocalDate end = promptDate("Date de fin (yyyy-MM-dd) : ");
        List<Paiement> paiements = statisticsController.paymentsBetween(start, end);
        if (paiements.isEmpty()) {
            logInfo("Aucun paiement sur cette période.");
            return;
        }
        logSection("Paiements trouvés");
        paiements.forEach(paiement -> logInfo(String.format("[%d] %s - %.2f - %s",
                paiement.getIdPaiement(),
                paiement.getType(),
                paiement.getMontant(),
                paiement.getDate())));
    }

    private void detectUnusualPayment() {
        double threshold = promptDouble("Seuil à surveiller : ");
        statisticsController.detectUnusualPayment(threshold)
                .ifPresentOrElse(
                        paiement -> logInfo(String.format("Paiement suspect : %.2f (agent %d, %s)",
                                paiement.getMontant(), paiement.getAgentId(), paiement.getDate())),
                        () -> logInfo("Aucun paiement au-dessus du seuil."));
    }

    private TypePaiement promptTypePaiement() {
        logInfo("Types disponibles :");
        Arrays.stream(TypePaiement.values()).forEach(value -> logInfo("- " + value));
        while (true) {
            String input = prompt("Type : ").trim().toUpperCase();
            try {
                return TypePaiement.valueOf(input);
            } catch (IllegalArgumentException e) {
                logInfo("Type invalide.");
            }
        }
    }
}
