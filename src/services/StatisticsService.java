package src.services;

import src.dao.AgentDAO;
import src.dao.DepartementDAO;
import src.dao.PaiementDAO;
import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Paiement;
import src.models.TypePaiement;
import src.services.interfaces.IStatisticsService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StatisticsService implements IStatisticsService {
    private final PaiementDAO paiementDAO;
    private final AgentDAO agentDAO;
    private final DepartementDAO departementDAO;

    public StatisticsService(PaiementDAO paiementDAO, AgentDAO agentDAO, DepartementDAO departementDAO) {
        this.paiementDAO = paiementDAO;
        this.agentDAO = agentDAO;
        this.departementDAO = departementDAO;
    }

    public double getAnnualTotalForAgent(int agentId, int year) throws AgentNotFoundException {
        ensureAgentExists(agentId);
        return paiementDAO.findByAgentId(agentId)
                .stream()
                .filter(p -> p.getDate() != null && p.getDate().getYear() == year)
                .mapToDouble(Paiement::getMontant)
                .sum();
    }

    public long countPaymentsByType(int agentId, TypePaiement type) throws AgentNotFoundException {
        ensureAgentExists(agentId);
        return paiementDAO.findByAgentId(agentId)
                .stream()
                .filter(p -> p.getType() == type)
                .count();
    }

    public Optional<Paiement> getHighestPaymentForAgent(int agentId) throws AgentNotFoundException {
        ensureAgentExists(agentId);
        return paiementDAO.findByAgentId(agentId)
                .stream()
                .max(Comparator.comparingDouble(Paiement::getMontant));
    }

    public double getDepartmentTotal(int departementId) throws DepartementNotFoundException {
        ensureDepartementExists(departementId);
        return agentDAO.findByDepartementId(departementId)
                .stream()
                .flatMap(agent -> paiementDAO.findByAgentId(agent.getIdAgent()).stream())
                .mapToDouble(Paiement::getMontant)
                .sum();
    }

    public double getDepartmentAverageSalary(int departementId) throws DepartementNotFoundException {
        ensureDepartementExists(departementId);
        List<Double> salaries = agentDAO.findByDepartementId(departementId)
                .stream()
                .flatMap(agent -> paiementDAO.findByAgentId(agent.getIdAgent()).stream())
                .filter(p -> p.getType() == TypePaiement.SALAIRE)
                .map(Paiement::getMontant)
                .toList();

        if (salaries.isEmpty()) {
            return 0.0;
        }
        return salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public List<Agent> rankAgentsByTotalPayments() {
        return agentDAO.findAll()
                .stream()
                .sorted((a1, a2) -> Double.compare(totalPayments(a2.getIdAgent()), totalPayments(a1.getIdAgent())))
                .toList();
    }

    public Map<TypePaiement, Long> getPaymentDistribution() {
        return paiementDAO.findAll()
                .stream()
                .collect(Collectors.groupingBy(Paiement::getType, Collectors.counting()));
    }

    public double getGlobalPaymentsTotal() {
        return paiementDAO.findAll()
                .stream()
                .mapToDouble(Paiement::getMontant)
                .sum();
    }

    public long getTotalAgents() {
        return agentDAO.findAll().size();
    }

    public long getTotalDepartements() {
        return departementDAO.findAll().size();
    }

    public Optional<Paiement> detectUnusualPayment(double threshold) {
        return paiementDAO.findAll()
                .stream()
                .filter(p -> p.getMontant() > threshold)
                .findFirst();
    }

    public List<Paiement> getPaymentsBetween(LocalDate start, LocalDate end) {
        return paiementDAO.findByDateRange(start, end);
    }

    private void ensureAgentExists(int agentId) throws AgentNotFoundException {
        if (agentDAO.findById(agentId).isEmpty()) {
            throw new AgentNotFoundException("Agent introuvable (id=" + agentId + ")");
        }
    }

    private void ensureDepartementExists(int departementId) throws DepartementNotFoundException {
        if (departementDAO.findById(departementId).isEmpty()) {
            throw new DepartementNotFoundException("Département introuvable (id=" + departementId + ")");
        }
    }

    private double totalPayments(int agentId) {
        return paiementDAO.findByAgentId(agentId)
                .stream()
                .mapToDouble(Paiement::getMontant)
                .sum();
    }
}
