package src.services.interfaces;

import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Departement;
import src.models.Paiement;

import java.util.List;

public interface IDepartmentService {
    
    Departement createDepartement(String nom, Integer responsableId) throws AgentNotFoundException;
    
    Departement updateDepartement(int departementId, String nom, Integer responsableId) 
            throws DepartementNotFoundException, AgentNotFoundException;
    
    void deleteDepartement(int departementId) throws DepartementNotFoundException;
    
    Departement getDepartementById(int departementId) throws DepartementNotFoundException;
    
    List<Departement> getAllDepartements();
    
    List<Agent> getAgents(int departementId) throws DepartementNotFoundException;
    
    Departement assignResponsable(int departementId, int agentId) 
            throws AgentNotFoundException, DepartementNotFoundException;
    
    void addAgentToDepartement(int departementId, int agentId) 
            throws AgentNotFoundException, DepartementNotFoundException;
    
    void removeAgentFromDepartement(int departementId, int agentId) 
            throws AgentNotFoundException, DepartementNotFoundException;
    
    List<Paiement> getPaymentsForDepartement(int departementId) throws DepartementNotFoundException;
}
