package src.controllers;

import src.exceptions.AgentNotFoundException;
import src.exceptions.DepartementNotFoundException;
import src.models.Agent;
import src.models.Departement;
import src.models.Paiement;
import src.services.DepartementService;

import java.util.List;

public class DepartementController {

    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    public List<Departement> listDepartements() {
        return departementService.getAllDepartements();
    }

    public Departement createDepartement(String nom, Integer responsableId) throws AgentNotFoundException {
        return departementService.createDepartement(nom, responsableId);
    }

    public Departement updateDepartement(int departementId, String nom, Integer responsableId)
            throws DepartementNotFoundException, AgentNotFoundException {
        return departementService.updateDepartement(departementId, nom, responsableId);
    }

    public void deleteDepartement(int departementId) throws DepartementNotFoundException {
        departementService.deleteDepartement(departementId);
    }

    public Departement assignResponsable(int departementId, int agentId)
            throws AgentNotFoundException, DepartementNotFoundException {
        return departementService.assignResponsable(departementId, agentId);
    }

    public List<Agent> listAgents(int departementId) throws DepartementNotFoundException {
        return departementService.getAgents(departementId);
    }

    public List<Paiement> listPayments(int departementId) throws DepartementNotFoundException {
        return departementService.getPaymentsForDepartement(departementId);
    }
}
