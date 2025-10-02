package src.services;

import src.dao.AgentDAO;
import src.dao.PaiementDAO;
import src.exceptions.AgentNotFoundException;
import src.exceptions.InvalidPaymentConditionException;
import src.exceptions.NegativeAmountException;
import src.exceptions.PaiementNotFoundException;
import src.models.Agent;
import src.models.Paiement;
import src.models.TypePaiement;
import src.services.interfaces.IPaiementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PaiementService implements IPaiementService {
    private final PaiementDAO paiementDAO;
    private final AgentDAO agentDAO;

    public PaiementService(PaiementDAO paiementDAO, AgentDAO agentDAO) {
        this.paiementDAO = paiementDAO;
        this.agentDAO = agentDAO;
    }

    public Paiement createPaiement(int agentId, TypePaiement type, double montant, String motif, boolean conditionValidee, LocalDate date)
            throws AgentNotFoundException, NegativeAmountException, InvalidPaymentConditionException {

        Agent agent = fetchAgent(agentId);
        validateMontant(montant);

        Paiement paiement = new Paiement(type, montant, motif, agent);
        paiement.setAgentId(agentId);
        paiement.setConditionValidee(conditionValidee);
        if (date != null) {
            paiement.setDate(date);
        }

        validateEligibility(paiement, agent);

        return paiementDAO.save(paiement);
    }

    public Paiement updatePaiement(int paiementId, TypePaiement type, double montant, String motif, boolean conditionValidee, LocalDate date)
            throws PaiementNotFoundException, AgentNotFoundException, NegativeAmountException, InvalidPaymentConditionException {

        Paiement paiement = fetchPaiement(paiementId);
        validateMontant(montant);
        paiement.setType(type);
        paiement.setMontant(montant);
        paiement.setMotif(motif);
        paiement.setConditionValidee(conditionValidee);
        if (date != null) {
            paiement.setDate(date);
        }

        Agent agent = fetchAgent(paiement.getAgentId());
        paiement.setAgent(agent);
        validateEligibility(paiement, agent);

        paiementDAO.update(paiement);
        return paiement;
    }

    public void deletePaiement(int paiementId) throws PaiementNotFoundException {
        fetchPaiement(paiementId);
        paiementDAO.deleteById(paiementId);
    }

    public Paiement getPaiementById(int paiementId) throws PaiementNotFoundException, AgentNotFoundException {
        Paiement paiement = fetchPaiement(paiementId);
        Agent agent = fetchAgent(paiement.getAgentId());
        paiement.setAgent(agent);
        return paiement;
    }

    public List<Paiement> getAllPaiements() {
        return paiementDAO.findAll();
    }

    public List<Paiement> getPaiementsByAgent(int agentId) throws AgentNotFoundException {
        Agent agent = fetchAgent(agentId);
        List<Paiement> paiements = paiementDAO.findByAgentId(agentId);
        paiements.forEach(p -> p.setAgent(agent));
        return paiements;
    }

    public List<Paiement> getPaiementsByType(TypePaiement type) {
        return paiementDAO.findByType(type);
    }

    public List<Paiement> getPaiementsByDateRange(LocalDate start, LocalDate end) {
        return paiementDAO.findByDateRange(start, end);
    }

    public double calculateTotalByAgent(int agentId) throws AgentNotFoundException {
        return getPaiementsByAgent(agentId)
                .stream()
                .mapToDouble(Paiement::getMontant)
                .sum();
    }

    public double calculateAverageByAgent(int agentId) throws AgentNotFoundException {
        List<Paiement> paiements = getPaiementsByAgent(agentId);
        if (paiements.isEmpty()) {
            return 0.0;
        }
        return paiements.stream()
                .mapToDouble(Paiement::getMontant)
                .average()
                .orElse(0.0);
    }

    /**
     * Validates payment amount.
     * @throws NegativeAmountException if amount is negative or zero
     * @throws IllegalArgumentException if amount exceeds maximum limit
     */
    private void validateMontant(double montant) throws NegativeAmountException {
        //validate pay amount
        if (montant < 0) {
            throw new NegativeAmountException("Le montant ne peut pas être négatif");
        }

        if (montant == 0) {
            throw new NegativeAmountException("amount should not be zero");
        }

        if (montant > 9999999.00) {
            throw new IllegalArgumentException("montant limit is 10,2");
        }
    }

    /**
     * Validates eligibility for bonus and indemnite payments.
     * @throws InvalidPaymentConditionException if agent is not eligible or condition not validated
     */
    private void validateEligibility(Paiement paiement, Agent agent) throws InvalidPaymentConditionException {
        //validate eligibility 4 bonus
        if ((paiement.getType() == TypePaiement.BONUS || paiement.getType() == TypePaiement.INDEMNITE)) {
            if (!agent.isEligibleForBonus()) {
                throw new InvalidPaymentConditionException("Agent non éligible pour " + paiement.getType());
            }
            if (!paiement.isConditionValidee()) {
                throw new InvalidPaymentConditionException("La condition doit être validée pour " + paiement.getType());
            }
        }
    }

    private Agent fetchAgent(int agentId) throws AgentNotFoundException {
        return agentDAO.findById(agentId)
                .orElseThrow(() -> new AgentNotFoundException("Agent introuvable (id=" + agentId + ")"));
    }

    private Paiement fetchPaiement(int paiementId) throws PaiementNotFoundException {
        return paiementDAO.findById(paiementId)
                .orElseThrow(() -> new PaiementNotFoundException("Paiement introuvable (id=" + paiementId + ")"));
    }
}
