package src.services;

import src.config.ConfigDBConn;
import src.dao.AgentDAO;
import src.dao.DepartementDAO;
import src.dao.PaiementDAO;
import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Departement;
import src.models.Paiement;
import src.models.TypeAgent;
import src.services.interfaces.IAgentService;

import java.util.List;
import java.util.Optional;


public class AgentService implements IAgentService {

    private final AgentDAO agentDAO;
    private final DepartementDAO departementDAO;
    private final PaiementDAO paiementDAO;

    public AgentService(AgentDAO agentDAO, DepartementDAO departementDAO, PaiementDAO paiementDAO) {
        this.agentDAO = agentDAO;
        this.departementDAO = departementDAO;
        this.paiementDAO = paiementDAO;
    }


    public Agent createAgent(String nom, String prenom, String email, String motDePasse, TypeAgent typeAgent, Integer departementId) throws DepartementNotFoundException {

        validateAgentInput(nom, prenom, email, motDePasse);
        ensureEmailAvailable(email, null);

        Agent agent = new Agent(nom, prenom, email, motDePasse, typeAgent);

        if (departementId != null) {
            Departement departement = fetchDepartement(departementId);
            agent.setDepartement(departement);
        }

        return agentDAO.save(agent);
    }

    public Agent updateAgent(int agentId, String nom, String prenom, String email, String motDePasse, TypeAgent typeAgent, Integer departementId) throws AgentNotFoundException, DepartementNotFoundException {

        Agent existingAgent = fetchAgent(agentId);
        
        validateAgentInput(nom, prenom, email, motDePasse);
        ensureEmailAvailable(email, agentId);

        existingAgent.setNom(nom);
        existingAgent.setPrenom(prenom);
        existingAgent.setEmail(email);
        existingAgent.setMotDePasse(motDePasse);
        existingAgent.setTypeAgent(typeAgent);

        if (departementId != null) {
            Departement departement = fetchDepartement(departementId);
            existingAgent.setDepartement(departement);
        } else {
            existingAgent.setDepartement(null);
        }

        agentDAO.update(existingAgent);
        return existingAgent;
    }

    public void deleteAgent(int agentId) throws AgentNotFoundException {
        fetchAgent(agentId);
        agentDAO.deleteById(agentId);
    }

    public Agent getAgentById(int agentId) throws AgentNotFoundException {
        return fetchAgent(agentId);
    }

    public List<Agent> getAllAgents() {
        return agentDAO.findAll();
    }

    public List<Agent> getAgentsByDepartment(int departementId) throws DepartementNotFoundException {
        fetchDepartement(departementId);
        return agentDAO.findByDepartementId(departementId);
    }

    public List<Agent> getAgentsByType(TypeAgent typeAgent) {
        return agentDAO.findByTypeAgent(typeAgent.name());
    }

    public Departement assignResponsable(int departementId, int agentId) throws AgentNotFoundException, DepartementNotFoundException {
        Departement departement = fetchDepartement(departementId);
        Agent agent = fetchAgent(agentId);

        if (agent.getDepartement() == null || agent.getDepartement().getIdDepartement() != departementId) {
            agent.setDepartement(departement);
            agentDAO.update(agent);
        }

        departement.setResponsable(agent);
        departementDAO.update(departement);
        return departement;
    }

    public List<Paiement> getPaymentsForAgent(int agentId) throws AgentNotFoundException {
        fetchAgent(agentId);
        return paiementDAO.findByAgentId(agentId);
    }

    public double calculateTotalPayments(int agentId) throws AgentNotFoundException {
        return getPaymentsForAgent(agentId)
                .stream()
                .mapToDouble(Paiement::getMontant)
                .sum();
    }

    public boolean testDatabaseConnection() {
        return ConfigDBConn.testConnection();
    }

    private Agent fetchAgent(int agentId) throws AgentNotFoundException {
        return agentDAO.findById(agentId)
                .orElseThrow(() -> new AgentNotFoundException("Agent introuvable (id=" + agentId + ")"));
    }

    private Departement fetchDepartement(int departementId) throws DepartementNotFoundException {
        return departementDAO.findById(departementId)
                .orElseThrow(() -> new DepartementNotFoundException("Département introuvable (id=" + departementId + ")"));
    }

    private void ensureEmailAvailable(String email, Integer currentAgentId) {
        Optional<Agent> existing = agentDAO.findByEmail(email);
        if (existing.isPresent() && (currentAgentId == null || existing.get().getIdAgent() != currentAgentId)) {
            throw new IllegalArgumentException("email déjà utilisé : " + email);
        }
    }

    private void validateAgentInput(String nom, String prenom, String email, String motDePasse) {
        if (nom == null) {
            throw new IllegalArgumentException("nom cant be NULL");
        }
        if (nom.trim().isEmpty()) {
            throw new IllegalArgumentException("nom cant be empty");
        }
        if (nom.length() > 100) {
            throw new IllegalArgumentException("nom longer than 100 chars");
        }

        if (prenom == null) {
            throw new IllegalArgumentException("prenom cant be NULL");
        }
        if (prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("prenom cant be empty");
        }

        if (email == null) {
            throw new IllegalArgumentException("email cant be NULL");
        }
        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("email cant be empty");
        }
        if (email.length() > 255) {
            throw new IllegalArgumentException("email cant be longer than 255 chars");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("format of email invalide");
        }

        if (motDePasse == null) {
            throw new IllegalArgumentException("pswrd cant be NULL");
        }
        if (motDePasse.trim().isEmpty()) {
            throw new IllegalArgumentException("pswrd cant be empty");
        }
        if (motDePasse.length() < 4) {
            throw new IllegalArgumentException("pswrd should be at least 4 chars");
        }
    }
}
