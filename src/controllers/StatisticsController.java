package src.controllers;

import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Paiement;
import src.models.TypePaiement;
import src.services.StatisticsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public double getAnnualTotalForAgent(int agentId, int year) throws AgentNotFoundException {
        return statisticsService.getAnnualTotalForAgent(agentId, year);
    }

    public long countPaymentsByTypeForAgent(int agentId, TypePaiement type) throws AgentNotFoundException {
        return statisticsService.countPaymentsByType(agentId, type);
    }

    public Optional<Paiement> highestPaymentForAgent(int agentId) throws AgentNotFoundException {
        return statisticsService.getHighestPaymentForAgent(agentId);
    }

    public double departmentTotal(int departementId) throws DepartementNotFoundException {
        return statisticsService.getDepartmentTotal(departementId);
    }

    public double departmentAverageSalary(int departementId) throws DepartementNotFoundException {
        return statisticsService.getDepartmentAverageSalary(departementId);
    }

    public List<Agent> rankAgentsByTotalPayments() {
        return statisticsService.rankAgentsByTotalPayments();
    }

    public Map<TypePaiement, Long> paymentDistribution() {
        return statisticsService.getPaymentDistribution();
    }

    public double globalPaymentsTotal() {
        return statisticsService.getGlobalPaymentsTotal();
    }

    public long totalAgents() {
        return statisticsService.getTotalAgents();
    }

    public long totalDepartements() {
        return statisticsService.getTotalDepartements();
    }

    public Optional<Paiement> detectUnusualPayment(double threshold) {
        return statisticsService.detectUnusualPayment(threshold);
    }

    public List<Paiement> paymentsBetween(LocalDate start, LocalDate end) {
        return statisticsService.getPaymentsBetween(start, end);
    }
}
