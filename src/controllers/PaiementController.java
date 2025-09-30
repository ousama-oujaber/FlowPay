package src.controllers;

import src.exceptions.AgentNotFoundException;
import src.exceptions.InvalidPaymentConditionException;
import src.exceptions.NegativeAmountException;
import src.exceptions.PaiementNotFoundException;
import src.models.Paiement;
import src.models.TypePaiement;
import src.services.PaiementService;

import java.time.LocalDate;
import java.util.List;

public class PaiementController {

    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    public Paiement createPaiement(int agentId,
                                   TypePaiement type,
                                   double montant,
                                   String motif,
                                   boolean conditionValidee,
                                   LocalDate date)
            throws AgentNotFoundException, NegativeAmountException, InvalidPaymentConditionException {
        return paiementService.createPaiement(agentId, type, montant, motif, conditionValidee, date);
    }

    public Paiement updatePaiement(int paiementId,
                                   TypePaiement type,
                                   double montant,
                                   String motif,
                                   boolean conditionValidee,
                                   LocalDate date)
            throws PaiementNotFoundException, AgentNotFoundException, NegativeAmountException, InvalidPaymentConditionException {
        return paiementService.updatePaiement(paiementId, type, montant, motif, conditionValidee, date);
    }

    public void deletePaiement(int paiementId) throws PaiementNotFoundException {
        paiementService.deletePaiement(paiementId);
    }

    public Paiement getPaiement(int paiementId) throws PaiementNotFoundException, AgentNotFoundException {
        return paiementService.getPaiementById(paiementId);
    }

    public List<Paiement> listPaiements() {
        return paiementService.getAllPaiements();
    }

    public List<Paiement> listPaiementsForAgent(int agentId) throws AgentNotFoundException {
        return paiementService.getPaiementsByAgent(agentId);
    }

    public List<Paiement> listPaiementsByType(TypePaiement type) {
        return paiementService.getPaiementsByType(type);
    }

    public List<Paiement> listPaiementsByDateRange(LocalDate start, LocalDate end) {
        return paiementService.getPaiementsByDateRange(start, end);
    }

    public double computeTotalByAgent(int agentId) throws AgentNotFoundException {
        return paiementService.calculateTotalByAgent(agentId);
    }

    public double computeAverageByAgent(int agentId) throws AgentNotFoundException {
        return paiementService.calculateAverageByAgent(agentId);
    }
}
