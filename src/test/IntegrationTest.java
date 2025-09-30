package src.test;

import src.config.ConfigDBConn;
import src.dao.*;
import src.services.*;

public class IntegrationTest {
    public static void main(String[] args) {
        System.out.println("=== FlowPay Integration Test ===\n");
        
        // Test 1: Database Connection
        System.out.println("Test 1: Database Connection");
        boolean dbConnected = ConfigDBConn.testConnection();
        System.out.println(dbConnected ? "✓ PASSED: Database connected" : "✗ FAILED: Database not connected");
        
        if (!dbConnected) {
            System.out.println("Cannot proceed without database connection.");
            System.exit(1);
        }
        
        // Test 2: Initialize DAOs
        System.out.println("\nTest 2: Initialize DAOs");
        try {
            AgentDAO agentDAO = new AgentDAO();
            DepartementDAO departementDAO = new DepartementDAO();
            PaiementDAO paiementDAO = new PaiementDAO();
            System.out.println("✓ PASSED: All DAOs initialized");
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            System.exit(1);
        }
        
        // Test 3: Initialize Services
        System.out.println("\nTest 3: Initialize Services");
        try {
            AgentDAO agentDAO = new AgentDAO();
            DepartementDAO departementDAO = new DepartementDAO();
            PaiementDAO paiementDAO = new PaiementDAO();
            
            SessionService sessionService = new SessionService();
            AuthService authService = new AuthService(agentDAO, sessionService);
            AgentService agentService = new AgentService(agentDAO, departementDAO, paiementDAO);
            DepartementService departementService = new DepartementService(departementDAO, agentDAO, paiementDAO);
            PaiementService paiementService = new PaiementService(paiementDAO, agentDAO);
            StatisticsService statisticsService = new StatisticsService(paiementDAO, agentDAO, departementDAO);
            
            System.out.println("✓ PASSED: All services initialized");
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            System.exit(1);
        }
        
        // Test 4: Verify Data Exists
        System.out.println("\nTest 4: Verify Database Data");
        try {
            AgentDAO agentDAO = new AgentDAO();
            DepartementDAO departementDAO = new DepartementDAO();
            PaiementDAO paiementDAO = new PaiementDAO();
            
            int agents = agentDAO.findAll().size();
            int depts = departementDAO.findAll().size();
            int paiements = paiementDAO.findAll().size();
            
            System.out.println("  - Agents: " + agents);
            System.out.println("  - Departements: " + depts);
            System.out.println("  - Paiements: " + paiements);
            
            if (agents > 0 && depts > 0) {
                System.out.println("✓ PASSED: Database has data");
            } else {
                System.out.println("⚠ WARNING: Database is empty");
            }
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            System.exit(1);
        }
        
        // Test 5: Interface Implementation
        System.out.println("\nTest 5: Interface Implementation");
        try {
            AgentDAO agentDAO = new AgentDAO();
            DepartementDAO departementDAO = new DepartementDAO();
            PaiementDAO paiementDAO = new PaiementDAO();
            
            // Test with interfaces
            src.services.interfaces.IAgentService agentService = new AgentService(agentDAO, departementDAO, paiementDAO);
            src.services.interfaces.IDepartmentService deptService = new DepartementService(departementDAO, agentDAO, paiementDAO);
            src.services.interfaces.IPaiementService paiementService = new PaiementService(paiementDAO, agentDAO);
            src.services.interfaces.IStatisticsService statsService = new StatisticsService(paiementDAO, agentDAO, departementDAO);
            
            // Test SessionService and AuthService separately
            SessionService sessionServiceImpl = new SessionService();
            src.services.interfaces.ISessionService sessionService = sessionServiceImpl;
            src.services.interfaces.IAuthService authService = new AuthService(agentDAO, sessionServiceImpl);
            
            System.out.println("✓ PASSED: All services implement their interfaces correctly");
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            System.exit(1);
        }
        
        System.out.println("\n=================================");
        System.out.println("✓ ALL INTEGRATION TESTS PASSED!");
        System.out.println("=================================");
        System.out.println("\nThe FlowPay application is ready to run.");
        System.out.println("Start the application with: java -cp \".:lib/mysql-connector-j-8.0.33.jar\" src.App");
    }
}
