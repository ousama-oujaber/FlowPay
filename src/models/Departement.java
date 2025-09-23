package src.models;

import java.util.ArrayList;
import java.util.List;

public class Departement {
    private int idDepartement;
    private String nom;
    private Agent responsable;
    private List<Agent> agents;

    public Departement(String nom) {
        this.nom = nom;
        this.agents = new ArrayList<>();
    }

    public int getIdDepartement() { return idDepartement; }
    public void setIdDepartement(int idDepartement) { this.idDepartement = idDepartement; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Agent getResponsable() { return responsable; }
    public void setResponsable(Agent responsable) { this.responsable = responsable; }
    public List<Agent> getAgents() { return agents; }
    public void setAgents(List<Agent> agents) { this.agents = agents; }

    public void addAgent(Agent agent) {
        this.agents.add(agent);
        agent.setDepartement(this);
    }

    public void removeAgent(Agent agent) {
        this.agents.remove(agent);
        agent.setDepartement(null);
    }
}
