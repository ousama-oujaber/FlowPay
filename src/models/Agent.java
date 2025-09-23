package src.models;

import java.util.ArrayList;
import java.util.List;

public class Agent extends Personne {
    private int idAgent;
    private TypeAgent typeAgent;
    private Departement departement;
    private List<Paiement> paiements;

    public Agent(String nom, String prenom, String email, String motDePasse, TypeAgent typeAgent) {
        super(nom, prenom, email, motDePasse);
        this.typeAgent = typeAgent;
        this.paiements = new ArrayList<>();
    }

    public int getIdAgent() { return idAgent; }
    public void setIdAgent(int idAgent) { this.idAgent = idAgent; }
    public TypeAgent getTypeAgent() { return typeAgent; }
    public void setTypeAgent(TypeAgent typeAgent) { this.typeAgent = typeAgent; }
    public Departement getDepartement() { return departement; }
    public void setDepartement(Departement departement) { this.departement = departement; }
    public List<Paiement> getPaiements() { return paiements; }
    public void setPaiements(List<Paiement> paiements) { this.paiements = paiements; }

    public void addPaiement(Paiement paiement) {
        this.paiements.add(paiement);
    }

    public boolean isEligibleForBonus() {
        return typeAgent == TypeAgent.RESPONSABLE_DEPARTEMENT || typeAgent == TypeAgent.DIRECTEUR;
    }
}
