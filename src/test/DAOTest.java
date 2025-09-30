package src.test;

import src.dao.*;
import src.models.*;
import java.util.List;
import java.util.Optional;

public class DAOTest {
    public static void main(String[] args) {
        System.out.println("=== Testing DAO Implementations ===\n");
        
        // Test DepartementDAO
        testDepartementDAO();
        
        // Test AgentDAO  
        testAgentDAO();
        
        // Test PaiementDAO
        testPaiementDAO();
        
        System.out.println("=== All DAO Tests Completed ===");
    }
    
    private static void testDepartementDAO() {
        System.out.println("--- Testing DepartementDAO ---");
        DepartementDAO departementDAO = new DepartementDAO();
        
        try {
            // Test findAll
            List<Departement> departements = departementDAO.findAll();
            System.out.println("Found " + departements.size() + " departements");
            
            // Test findById
            if (!departements.isEmpty()) {
                Optional<Departement> dept = departementDAO.findById(departements.get(0).getIdDepartement());
                System.out.println("Found departement by ID: " + dept.isPresent());
            }
            
            // Test findByNom
            Optional<Departement> deptByName = departementDAO.findByNom("IT");
            System.out.println("Found IT departement: " + deptByName.isPresent());
            
            System.out.println("DepartementDAO tests passed!\n");
        } catch (Exception e) {
            System.out.println("DepartementDAO test error: " + e.getMessage());
        }
    }
    
    private static void testAgentDAO() {
        System.out.println("--- Testing AgentDAO ---");
        AgentDAO agentDAO = new AgentDAO();
        
        try {
            // Test findAll
            List<Agent> agents = agentDAO.findAll();
            System.out.println("Found " + agents.size() + " agents");
            
            // Test findById
            if (!agents.isEmpty()) {
                Optional<Agent> agent = agentDAO.findById(agents.get(0).getIdAgent());
                System.out.println("Found agent by ID: " + agent.isPresent());
            }
            
            // Test findByTypeAgent
            List<Agent> ouvrierAgents = agentDAO.findByTypeAgent("OUVRIER");
            System.out.println("Found " + ouvrierAgents.size() + " ouvrier agents");
            
            System.out.println("AgentDAO tests passed!\n");
        } catch (Exception e) {
            System.out.println("AgentDAO test error: " + e.getMessage());
        }
    }
    
    private static void testPaiementDAO() {
        System.out.println("--- Testing PaiementDAO ---");
        PaiementDAO paiementDAO = new PaiementDAO();
        
        try {
            // Test findAll
            List<Paiement> paiements = paiementDAO.findAll();
            System.out.println("Found " + paiements.size() + " paiements");
            
            // Test findById
            if (!paiements.isEmpty()) {
                Optional<Paiement> paiement = paiementDAO.findById(paiements.get(0).getIdPaiement());
                System.out.println("Found paiement by ID: " + paiement.isPresent());
            }
            
            // Test findByType
            List<Paiement> salaires = paiementDAO.findByType(TypePaiement.SALAIRE);
            System.out.println("Found " + salaires.size() + " salaire payments");
            
            System.out.println("PaiementDAO tests passed!\n");
        } catch (Exception e) {
            System.out.println("PaiementDAO test error: " + e.getMessage());
        }
    }
}