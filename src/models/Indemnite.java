package src.models;

public class Indemnite extends Paiement {
    public Indemnite(double montant, String motif, Agent agent) {
        super(TypePaiement.INDEMNITE, montant, motif, agent);
    }
}
