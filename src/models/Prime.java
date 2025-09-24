package src.models;

public class Prime extends Paiement {
    public Prime(double montant, String motif, Agent agent) {
        super(TypePaiement.PRIME, montant, motif, agent);
    }
}