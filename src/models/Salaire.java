package src.models;

public class Salaire extends Paiement {
    public Salaire(double montant, String motif, Agent agent) {
        super(TypePaiement.SALAIRE, montant, motif, agent);
    }
}
