package src.services.interfaces;

import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Paiement;
import src.models.TypePaiement;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for statistical analysis and reporting.
 * Provides comprehensive analytics for agents, departments, and payments.
 */
public interface IStatisticsService {
    //agent stats
    double getAnnualTotalForAgent(int agentId, int year) throws AgentNotFoundException;
    
    long countPaymentsByType(int agentId, TypePaiement type) throws AgentNotFoundException;
    
    Optional<Paiement> getHighestPaymentForAgent(int agentId) throws AgentNotFoundException;
    
    //department stats
    double getDepartmentTotal(int departementId) throws DepartementNotFoundException;
    
    double getDepartmentAverageSalary(int departementId) throws DepartementNotFoundException;
    
    List<Agent> rankAgentsByTotalPayments();
    
    Map<TypePaiement, Long> getPaymentDistribution();
    
    double getGlobalPaymentsTotal();
    
    long getTotalAgents();
    
    long getTotalDepartements();
    
    Optional<Paiement> detectUnusualPayment(double threshold);
    
    List<Paiement> getPaymentsBetween(LocalDate start, LocalDate end);
}
