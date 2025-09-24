package src.models;

import java.time.LocalDate;

public class Paiement {
    private int idPaiement;
    private TypePaiement type;
    private double montant;
    private LocalDate date;
    private String motif;
    private Agent agent;
    private boolean conditionValidee;
    private int agentId;

    public Paiement(TypePaiement type, double montant, String motif, Agent agent) {
        this.type = type;
        this.montant = montant;
        this.motif = motif;
        this.agent = agent;
        this.date = LocalDate.now();
        this.conditionValidee = false;
        this.agentId = agent != null ? agent.getIdAgent() : 0;
    }

    public int getIdPaiement() { return idPaiement; }
    public void setIdPaiement(int idPaiement) { this.idPaiement = idPaiement; }
    public TypePaiement getType() { return type; }
    public void setType(TypePaiement type) { this.type = type; }
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) {
        this.agent = agent;
        this.agentId = agent != null ? agent.getIdAgent() : 0;
    }
    public boolean isConditionValidee() { return conditionValidee; }
    public void setConditionValidee(boolean conditionValidee) { this.conditionValidee = conditionValidee; }
    public int getAgentId() { return agentId; }
    public void setAgentId(int agentId) { this.agentId = agentId; }

    public boolean isEligible() {
        if (type == TypePaiement.BONUS || type == TypePaiement.INDEMNITE) {
            return agent != null && agent.isEligibleForBonus() && conditionValidee;
        }
        return true;
    }
}