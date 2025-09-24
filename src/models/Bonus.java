package src.models;

public class Bonus extends Paiement {
    public Bonus(double montant, String motif, Agent agent) {
        super(TypePaiement.BONUS, montant, motif, agent);
    }
}
