package src.controllers;

import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Paiement;
import src.models.TypeAgent;
import src.services.AgentService;

import java.util.List;

public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    public List<Agent> listAgents() {
        return agentService.getAllAgents();
    }

    public Agent getAgent(int agentId) throws AgentNotFoundException {
        return agentService.getAgentById(agentId);
    }

    public Agent createAgent(String nom,
                             String prenom,
                             String email,
                             String motDePasse,
                             TypeAgent typeAgent,
                             Integer departementId) throws DepartementNotFoundException {
        return agentService.createAgent(nom, prenom, email, motDePasse, typeAgent, departementId);
    }

    public Agent updateAgent(int agentId,
                             String nom,
                             String prenom,
                             String email,
                             String motDePasse,
                             TypeAgent typeAgent,
                             Integer departementId) throws AgentNotFoundException, DepartementNotFoundException {
        return agentService.updateAgent(agentId, nom, prenom, email, motDePasse, typeAgent, departementId);
    }

    public void deleteAgent(int agentId) throws AgentNotFoundException {
        agentService.deleteAgent(agentId);
    }

    public List<Paiement> listPaymentsForAgent(int agentId) throws AgentNotFoundException {
        return agentService.getPaymentsForAgent(agentId);
    }

    public double computeTotalPayments(int agentId) throws AgentNotFoundException {
        return agentService.calculateTotalPayments(agentId);
    }
}
