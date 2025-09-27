package src.services.interfaces;

import src.models.Agent;
import src.exceptions.*;
import src.models.Departement;
import src.models.Paiement;
import src.models.TypeAgent;

import java.util.List;

public interface IAgentService {

    Agent createAgent(String nom, String prenom, String email, String motDePasse,
                      TypeAgent typeAgent, Integer departementId) throws DepartementNotFoundException;

    Agent updateAgent(int agentId, String nom, String prenom, String email, String motDePasse,
                      TypeAgent typeAgent, Integer departementId) throws AgentNotFoundException, DepartementNotFoundException;

    void deleteAgent(int agentId) throws AgentNotFoundException;

    Agent getAgentById(int agentId) throws AgentNotFoundException;

    List<Agent> getAllAgents();

    List<Agent> getAgentsByDepartment(int departementId) throws DepartementNotFoundException;

    List<Agent> getAgentsByType(TypeAgent typeAgent);

    Departement assignResponsable(int departementId, int agentId) throws AgentNotFoundException, DepartementNotFoundException;

    List<Paiement> getPaymentsForAgent(int agentId) throws AgentNotFoundException;

    double calculateTotalPayments(int agentId) throws AgentNotFoundException;

    boolean testDatabaseConnection();
}
