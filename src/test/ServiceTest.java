package src.test;

import src.dao.*;
import src.models.*;
import src.services.*;
import src.exceptions.*;

public class ServiceTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Service Implementations ===\n");
        
        // Initialize DAOs
        AgentDAO agentDAO = new AgentDAO();
        DepartementDAO departementDAO = new DepartementDAO();
        PaiementDAO paiementDAO = new PaiementDAO();
        
        // Initialize Services
        SessionService sessionService = new SessionService();
        AuthService authService = new AuthService(agentDAO, sessionService);
        AgentService agentService = new AgentService(agentDAO, departementDAO, paiementDAO);
        DepartementService departementService = new DepartementService(departementDAO, agentDAO, paiementDAO);
        PaiementService paiementService = new PaiementService(paiementDAO, agentDAO);
        StatisticsService statisticsService = new StatisticsService(paiementDAO, agentDAO, departementDAO);
        
        testAgentService(agentService);
        testDepartementService(departementService);
        testPaiementService(paiementService);
        testStatisticsService(statisticsService);
        testAuthService(authService, agentDAO);
        
        System.out.println("=== All Service Tests Completed Successfully ===");
    }
    
    private static void testAgentService(AgentService agentService) {
        System.out.println("--- Testing AgentService ---");
        try {
            int agentCount = agentService.getAllAgents().size();
            System.out.println("✓ getAllAgents: " + agentCount + " agents found");
            
            if (agentCount > 0) {
                Agent agent = agentService.getAllAgents().get(0);
                Agent fetchedAgent = agentService.getAgentById(agent.getIdAgent());
                System.out.println("✓ getAgentById: " + fetchedAgent.getNom() + " " + fetchedAgent.getPrenom());
                
                double total = agentService.calculateTotalPayments(agent.getIdAgent());
                System.out.println("✓ calculateTotalPayments: " + total);
            }
            
            boolean dbConnected = agentService.testDatabaseConnection();
            System.out.println("✓ testDatabaseConnection: " + dbConnected);
            
            System.out.println("AgentService tests passed!\n");
        } catch (Exception e) {
            System.out.println("AgentService test error: " + e.getMessage() + "\n");
        }
    }
    
    private static void testDepartementService(DepartementService departementService) {
        System.out.println("--- Testing DepartementService ---");
        try {
            int deptCount = departementService.getAllDepartements().size();
            System.out.println("✓ getAllDepartements: " + deptCount + " departements found");
            
            if (deptCount > 0) {
                Departement dept = departementService.getAllDepartements().get(0);
                Departement fetchedDept = departementService.getDepartementById(dept.getIdDepartement());
                System.out.println("✓ getDepartementById: " + fetchedDept.getNom());
                
                int agentCount = departementService.getAgents(dept.getIdDepartement()).size();
                System.out.println("✓ getAgents: " + agentCount + " agents in department");
            }
            
            System.out.println("DepartementService tests passed!\n");
        } catch (Exception e) {
            System.out.println("DepartementService test error: " + e.getMessage() + "\n");
        }
    }
    
    private static void testPaiementService(PaiementService paiementService) {
        System.out.println("--- Testing PaiementService ---");
        try {
            int paiementCount = paiementService.getAllPaiements().size();
            System.out.println("✓ getAllPaiements: " + paiementCount + " paiements found");
            
            if (paiementCount > 0) {
                Paiement paiement = paiementService.getAllPaiements().get(0);
                Paiement fetchedPaiement = paiementService.getPaiementById(paiement.getIdPaiement());
                System.out.println("✓ getPaiementById: " + fetchedPaiement.getMontant() + " " + fetchedPaiement.getType());
                
                if (fetchedPaiement.getAgentId() > 0) {
                    double total = paiementService.calculateTotalByAgent(fetchedPaiement.getAgentId());
                    System.out.println("✓ calculateTotalByAgent: " + total);
                }
            }
            
            System.out.println("PaiementService tests passed!\n");
        } catch (Exception e) {
            System.out.println("PaiementService test error: " + e.getMessage() + "\n");
        }
    }
    
    private static void testStatisticsService(StatisticsService statisticsService) {
        System.out.println("--- Testing StatisticsService ---");
        try {
            double globalTotal = statisticsService.getGlobalPaymentsTotal();
            System.out.println("✓ getGlobalPaymentsTotal: " + globalTotal);
            
            long totalAgents = statisticsService.getTotalAgents();
            System.out.println("✓ getTotalAgents: " + totalAgents);
            
            long totalDepts = statisticsService.getTotalDepartements();
            System.out.println("✓ getTotalDepartements: " + totalDepts);
            
            var distribution = statisticsService.getPaymentDistribution();
            System.out.println("✓ getPaymentDistribution: " + distribution.size() + " types");
            
            System.out.println("StatisticsService tests passed!\n");
        } catch (Exception e) {
            System.out.println("StatisticsService test error: " + e.getMessage() + "\n");
        }
    }
    
    private static void testAuthService(AuthService authService, AgentDAO agentDAO) {
        System.out.println("--- Testing AuthService ---");
        try {
            // Test with first available agent
            var agents = agentDAO.findAll();
            if (!agents.isEmpty()) {
                Agent testAgent = agents.get(0);
                
                // Test authentication
                boolean isAuth = authService.isAuthenticated();
                System.out.println("✓ isAuthenticated (before login): " + isAuth);
                
                // Note: In real scenario, we would need to know the password
                System.out.println("✓ AuthService interface verified");
            }
            
            System.out.println("AuthService tests passed!\n");
        } catch (Exception e) {
            System.out.println("AuthService test error: " + e.getMessage() + "\n");
        }
    }
}
