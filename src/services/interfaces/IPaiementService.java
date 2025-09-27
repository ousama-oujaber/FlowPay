package src.services.interfaces;

import src.exceptions.AgentNotFoundException;
import src.exceptions.InvalidPaymentConditionException;
import src.exceptions.NegativeAmountException;
import src.exceptions.PaiementNotFoundException;
import src.models.Paiement;
import src.models.TypePaiement;

import java.time.LocalDate;
import java.util.List;

public interface IPaiementService {

    Paiement createPaiement(int agentId, TypePaiement type, double montant, String motif,
                            boolean conditionValidee, LocalDate date)
            throws AgentNotFoundException, NegativeAmountException, InvalidPaymentConditionException;

    Paiement updatePaiement(int paiementId, TypePaiement type, double montant, String motif,
                            boolean conditionValidee, LocalDate date)
            throws PaiementNotFoundException, AgentNotFoundException, NegativeAmountException, InvalidPaymentConditionException;

    void deletePaiement(int paiementId) throws PaiementNotFoundException;

    Paiement getPaiementById(int paiementId) throws PaiementNotFoundException, AgentNotFoundException;

    List<Paiement> getAllPaiements();

    List<Paiement> getPaiementsByAgent(int agentId) throws AgentNotFoundException;

    List<Paiement> getPaiementsByType(TypePaiement type);

    List<Paiement> getPaiementsByDateRange(LocalDate start, LocalDate end);

    double calculateTotalByAgent(int agentId) throws AgentNotFoundException;

    double calculateAverageByAgent(int agentId) throws AgentNotFoundException;
}
