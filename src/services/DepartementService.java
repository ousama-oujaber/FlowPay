package src.services;

import src.dao.AgentDAO;
import src.dao.DepartementDAO;
import src.dao.PaiementDAO;
import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Departement;
import src.models.Paiement;
import src.services.interfaces.IDepartmentService;

import java.util.List;
import java.util.Optional;

public class DepartementService implements IDepartmentService {

	private final DepartementDAO departementDAO;
	private final AgentDAO agentDAO;
	private final PaiementDAO paiementDAO;

	public DepartementService(DepartementDAO departementDAO, AgentDAO agentDAO, PaiementDAO paiementDAO) {
		this.departementDAO = departementDAO;
		this.agentDAO = agentDAO;
		this.paiementDAO = paiementDAO;
	}

	public Departement createDepartement(String nom, Integer responsableId) throws AgentNotFoundException {
		validateDepartementName(nom);
		ensureNameAvailable(nom, null);

		Departement departement = new Departement(nom);

		if (responsableId != null) {
			Agent responsable = fetchAgent(responsableId);
			departement.setResponsable(responsable);
		}

		return departementDAO.save(departement);
	}

	public Departement updateDepartement(int departementId, String nom, Integer responsableId)
			throws DepartementNotFoundException, AgentNotFoundException {

		Departement departement = fetchDepartement(departementId);
		
		// Validate input BEFORE checking database
		validateDepartementName(nom);
		ensureNameAvailable(nom, departementId);
		departement.setNom(nom);

		if (responsableId != null) {
			Agent responsable = fetchAgent(responsableId);
			if (responsable.getDepartement() == null || responsable.getDepartement().getIdDepartement() != departementId) {
				responsable.setDepartement(departement);
				agentDAO.update(responsable);
			}
			departement.setResponsable(responsable);
		} else {
			departement.setResponsable(null);
		}

		departementDAO.update(departement);
		return departement;
	}

	public void deleteDepartement(int departementId) throws DepartementNotFoundException {
		fetchDepartement(departementId);
		unassignAgents(departementId);
		departementDAO.deleteById(departementId);
	}

	public Departement getDepartementById(int departementId) throws DepartementNotFoundException {
		return fetchDepartement(departementId);
	}

	public List<Departement> getAllDepartements() {
		return departementDAO.findAll();
	}

	public List<Agent> getAgents(int departementId) throws DepartementNotFoundException {
		fetchDepartement(departementId);
		return agentDAO.findByDepartementId(departementId);
	}

	public Departement assignResponsable(int departementId, int agentId)
			throws AgentNotFoundException, DepartementNotFoundException {
		return updateDepartement(departementId, fetchDepartement(departementId).getNom(), agentId);
	}

	public void addAgentToDepartement(int departementId, int agentId)
			throws AgentNotFoundException, DepartementNotFoundException {
		Departement departement = fetchDepartement(departementId);
		Agent agent = fetchAgent(agentId);
		agent.setDepartement(departement);
		agentDAO.update(agent);
	}

	public void removeAgentFromDepartement(int departementId, int agentId)
			throws AgentNotFoundException, DepartementNotFoundException {
		fetchDepartement(departementId);
		Agent agent = fetchAgent(agentId);
		agent.setDepartement(null);
		agentDAO.update(agent);
	}

	public List<Paiement> getPaymentsForDepartement(int departementId) throws DepartementNotFoundException {
		List<Agent> agents = getAgents(departementId);
		return agents.stream()
				.flatMap(agent -> paiementDAO.findByAgentId(agent.getIdAgent()).stream())
				.toList();
	}

	private Departement fetchDepartement(int departementId) throws DepartementNotFoundException {
		return departementDAO.findById(departementId)
				.orElseThrow(() -> new DepartementNotFoundException("Departement introuvable (id=" + departementId + ")"));
	}

	private Agent fetchAgent(int agentId) throws AgentNotFoundException {
		return agentDAO.findById(agentId)
				.orElseThrow(() -> new AgentNotFoundException("agent introuvable (id=" + agentId + ")"));
	}

	private void ensureNameAvailable(String nom, Integer currentId) {
		Optional<Departement> existing = departementDAO.findByNom(nom);
		if (existing.isPresent() && (currentId == null || existing.get().getIdDepartement() != currentId)) {
			throw new IllegalArgumentException("Nom of departement exist :) : " + nom);
		}
	}

	private void unassignAgents(int departementId) {
		List<Agent> agents = agentDAO.findByDepartementId(departementId);
		for (Agent agent : agents) {
			agent.setDepartement(null);
			agentDAO.update(agent);
		}
	}

	private void validateDepartementName(String nom) {
		if (nom == null) {
			throw new IllegalArgumentException("name of departement cant be NULL");
		}
		if (nom.trim().isEmpty()) {
			throw new IllegalArgumentException("name of departement cant be empty");
		}
        if (nom.length() > 100) {
            throw new IllegalArgumentException("name longer than 100 chars");
        }
	}
}
