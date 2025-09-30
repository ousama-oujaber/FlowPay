package src.test;

import src.dao.*;
import src.models.*;
import src.services.*;
import src.exceptions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Comprehensive Crash Testing Suite
 * Tests edge cases, null values, invalid inputs, and stress scenarios
 * to find potential crashes and vulnerabilities in the FlowPay system
 */
public class CrashTest {
    
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    private static List<String> crashedTests = new ArrayList<>();
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         FlowPay CRASH TEST SUITE                          ║");
        System.out.println("║         Testing for Crashes, Edge Cases & Bugs            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Initialize services
        AgentDAO agentDAO = new AgentDAO();
        DepartementDAO departementDAO = new DepartementDAO();
        PaiementDAO paiementDAO = new PaiementDAO();
        
        SessionService sessionService = new SessionService();
        AuthService authService = new AuthService(agentDAO, sessionService);
        AgentService agentService = new AgentService(agentDAO, departementDAO, paiementDAO);
        DepartementService departementService = new DepartementService(departementDAO, agentDAO, paiementDAO);
        PaiementService paiementService = new PaiementService(paiementDAO, agentDAO);
        StatisticsService statisticsService = new StatisticsService(paiementDAO, agentDAO, departementDAO);
        
        // Run all crash tests
        testNullInputs(agentService, departementService, paiementService, authService);
        testInvalidIds(agentService, departementService, paiementService);
        testNegativeValues(paiementService);
        testBoundaryValues(agentService, departementService, paiementService);
        testEmptyStrings(agentService, departementService, paiementService);
        testConcurrentAccess(agentDAO, departementDAO, paiementDAO);
        testInvalidDates(paiementService);
        testAuthenticationEdgeCases(authService, sessionService);
        testStatisticsEdgeCases(statisticsService);
        testDatabaseConnectionFailures();
        
        // Print final report
        printFinalReport();
    }
    
    // ==================== NULL INPUT TESTS ====================
    private static void testNullInputs(AgentService agentService, DepartementService departementService, 
                                       PaiementService paiementService, AuthService authService) {
        System.out.println("\n[TEST CATEGORY 1] Testing NULL Inputs");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 1: Create agent with null name
        runTest("Create Agent with NULL name", () -> {
            agentService.createAgent(null, "Test", "test@test.com", "pass", TypeAgent.OUVRIER, null);
        }, true);
        
        // Test 2: Create agent with null email
        runTest("Create Agent with NULL email", () -> {
            agentService.createAgent("John", "Doe", null, "pass", TypeAgent.OUVRIER, null);
        }, true);
        
        // Test 3: Create agent with null password
        runTest("Create Agent with NULL password", () -> {
            agentService.createAgent("John", "Doe", "john@test.com", null, TypeAgent.OUVRIER, null);
        }, true);
        
        // Test 4: Create agent with null type
        runTest("Create Agent with NULL TypeAgent", () -> {
            agentService.createAgent("John", "Doe", "john@test.com", "pass", null, null);
        }, true);
        
        // Test 5: Create departement with null name
        runTest("Create Departement with NULL name", () -> {
            departementService.createDepartement(null, null);
        }, true);
        
        // Test 6: Login with null email
        runTest("Login with NULL email", () -> {
            authService.login(null, "password");
        }, true);
        
        // Test 7: Login with null password
        runTest("Login with NULL password", () -> {
            authService.login("test@test.com", null);
        }, true);
        
        // Test 8: Create payment with null type
        runTest("Create Payment with NULL type", () -> {
            paiementService.createPaiement(1, null, 1000.0, "test", false, LocalDate.now());
        }, true);
    }
    
    // ==================== INVALID ID TESTS ====================
    private static void testInvalidIds(AgentService agentService, DepartementService departementService,
                                       PaiementService paiementService) {
        System.out.println("\n[TEST CATEGORY 2] Testing Invalid IDs");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 9: Get agent with negative ID
        runTest("Get Agent with ID = -1", () -> {
            agentService.getAgentById(-1);
        }, true);
        
        // Test 10: Get agent with zero ID
        runTest("Get Agent with ID = 0", () -> {
            agentService.getAgentById(0);
        }, true);
        
        // Test 11: Get agent with very large ID
        runTest("Get Agent with ID = 999999", () -> {
            agentService.getAgentById(999999);
        }, true);
        
        // Test 12: Delete non-existent agent
        runTest("Delete Agent with ID = 888888", () -> {
            agentService.deleteAgent(888888);
        }, true);
        
        // Test 13: Get departement with negative ID
        runTest("Get Departement with ID = -5", () -> {
            departementService.getDepartementById(-5);
        }, true);
        
        // Test 14: Delete non-existent departement
        runTest("Delete Departement with ID = 777777", () -> {
            departementService.deleteDepartement(777777);
        }, true);
        
        // Test 15: Get payment with invalid ID
        runTest("Get Payment with ID = -100", () -> {
            paiementService.getPaiementById(-100);
        }, true);
        
        // Test 16: Create payment for non-existent agent
        runTest("Create Payment for non-existent Agent (ID=999999)", () -> {
            paiementService.createPaiement(999999, TypePaiement.SALAIRE, 1000.0, "test", false, LocalDate.now());
        }, true);
    }
    
    // ==================== NEGATIVE VALUE TESTS ====================
    private static void testNegativeValues(PaiementService paiementService) {
        System.out.println("\n[TEST CATEGORY 3] Testing Negative Values");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 17: Create payment with negative amount
        runTest("Create Payment with amount = -1000.0", () -> {
            paiementService.createPaiement(1, TypePaiement.SALAIRE, -1000.0, "test", false, LocalDate.now());
        }, true);
        
        // Test 18: Create payment with zero amount
        runTest("Create Payment with amount = 0.0", () -> {
            paiementService.createPaiement(1, TypePaiement.SALAIRE, 0.0, "test", false, LocalDate.now());
        }, false);
        
        // Test 19: Create payment with very large amount
        runTest("Create Payment with amount = 999999999.99", () -> {
            paiementService.createPaiement(1, TypePaiement.SALAIRE, 999999999.99, "test", false, LocalDate.now());
        }, false);
    }
    
    // ==================== BOUNDARY VALUE TESTS ====================
    private static void testBoundaryValues(AgentService agentService, DepartementService departementService,
                                           PaiementService paiementService) {
        System.out.println("\n[TEST CATEGORY 4] Testing Boundary Values");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 20: Very long email
        runTest("Create Agent with 500-char email", () -> {
            String longEmail = "a".repeat(500) + "@test.com";
            agentService.createAgent("Test", "User", longEmail, "pass", TypeAgent.OUVRIER, null);
        }, false);
        
        // Test 21: Very long name
        runTest("Create Agent with 1000-char name", () -> {
            String longName = "x".repeat(1000);
            agentService.createAgent(longName, "User", "test@test.com", "pass", TypeAgent.OUVRIER, null);
        }, false);
        
        // Test 22: Very long departement name
        runTest("Create Departement with 2000-char name", () -> {
            String longName = "Department".repeat(200);
            departementService.createDepartement(longName, null);
        }, false);
        
        // Test 23: SQL injection attempt in name (should succeed - PreparedStatements prevent injection)
        runTest("Create Agent with SQL injection in name", () -> {
            agentService.createAgent("'; DROP TABLE agents; --", "User", "hack" + System.currentTimeMillis() + "@test.com", "pass", TypeAgent.OUVRIER, null);
        }, false);
    }
    
    // ==================== EMPTY STRING TESTS ====================
    private static void testEmptyStrings(AgentService agentService, DepartementService departementService,
                                        PaiementService paiementService) {
        System.out.println("\n[TEST CATEGORY 5] Testing Empty Strings");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 24: Empty name (should fail - validation rejects empty strings)
        runTest("Create Agent with empty name", () -> {
            agentService.createAgent("", "Test", "empty@test.com", "pass", TypeAgent.OUVRIER, null);
        }, true);
        
        // Test 25: Empty email (should fail - validation rejects empty strings)
        runTest("Create Agent with empty email", () -> {
            agentService.createAgent("John", "Doe", "", "pass", TypeAgent.OUVRIER, null);
        }, true);
        
        // Test 26: Empty password (should fail - validation rejects empty strings)
        runTest("Create Agent with empty password", () -> {
            agentService.createAgent("John", "Doe", "john@empty.com", "", TypeAgent.OUVRIER, null);
        }, true);
        
        // Test 27: Whitespace-only name (should fail - validation rejects whitespace)
        runTest("Create Agent with whitespace-only name", () -> {
            agentService.createAgent("   ", "Test", "space@test.com", "pass", TypeAgent.OUVRIER, null);
        }, true);
        
        // Test 28: Empty departement name (should fail - validation rejects empty strings)
        runTest("Create Departement with empty name", () -> {
            departementService.createDepartement("", null);
        }, true);
    }
    
    // ==================== CONCURRENT ACCESS TESTS ====================
    private static void testConcurrentAccess(AgentDAO agentDAO, DepartementDAO departementDAO, PaiementDAO paiementDAO) {
        System.out.println("\n[TEST CATEGORY 6] Testing Concurrent Access");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 29: Multiple threads reading agents
        runTest("10 threads reading agents concurrently", () -> {
            ExecutorService executor = Executors.newFixedThreadPool(10);
            List<Future<?>> futures = new ArrayList<>();
            
            for (int i = 0; i < 10; i++) {
                futures.add(executor.submit(() -> {
                    agentDAO.findAll();
                }));
            }
            
            for (Future<?> future : futures) {
                future.get(5, TimeUnit.SECONDS);
            }
            executor.shutdown();
        }, false);
        
        // Test 30: Multiple threads reading departements
        runTest("20 threads reading departements concurrently", () -> {
            ExecutorService executor = Executors.newFixedThreadPool(20);
            List<Future<?>> futures = new ArrayList<>();
            
            for (int i = 0; i < 20; i++) {
                futures.add(executor.submit(() -> {
                    departementDAO.findAll();
                }));
            }
            
            for (Future<?> future : futures) {
                future.get(5, TimeUnit.SECONDS);
            }
            executor.shutdown();
        }, false);
    }
    
    // ==================== INVALID DATE TESTS ====================
    private static void testInvalidDates(PaiementService paiementService) {
        System.out.println("\n[TEST CATEGORY 7] Testing Invalid Dates");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 31: Payment with future date (100 years ahead)
        runTest("Create Payment with date = 100 years in future", () -> {
            LocalDate futureDate = LocalDate.now().plusYears(100);
            paiementService.createPaiement(1, TypePaiement.SALAIRE, 1000.0, "future", false, futureDate);
        }, false);
        
        // Test 32: Payment with very old date
        runTest("Create Payment with date = 1900-01-01", () -> {
            LocalDate oldDate = LocalDate.of(1900, 1, 1);
            paiementService.createPaiement(1, TypePaiement.SALAIRE, 1000.0, "old", false, oldDate);
        }, false);
        
        // Test 33: Get payments with invalid date range
        runTest("Get Payments with end date before start date", () -> {
            LocalDate start = LocalDate.now();
            LocalDate end = LocalDate.now().minusDays(10);
            paiementService.getPaiementsByDateRange(start, end);
        }, false);
    }
    
    // ==================== AUTHENTICATION EDGE CASES ====================
    private static void testAuthenticationEdgeCases(AuthService authService, SessionService sessionService) {
        System.out.println("\n[TEST CATEGORY 8] Testing Authentication Edge Cases");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 34: Login with non-existent email
        runTest("Login with non-existent email", () -> {
            authService.login("nonexistent@fake.com", "password");
        }, true);
        
        // Test 35: Login with wrong password
        runTest("Login with wrong password", () -> {
            authService.login("admin@flowpay.com", "wrongpassword");
        }, true);
        
        // Test 36: Multiple logouts
        runTest("Logout twice consecutively", () -> {
            authService.logout();
            authService.logout();
        }, false);
        
        // Test 37: Check auth status without login
        runTest("Check authentication without login", () -> {
            boolean isAuth = authService.isAuthenticated();
            if (isAuth) throw new Exception("Should not be authenticated");
        }, false);
    }
    
    // ==================== STATISTICS EDGE CASES ====================
    private static void testStatisticsEdgeCases(StatisticsService statisticsService) {
        System.out.println("\n[TEST CATEGORY 9] Testing Statistics Edge Cases");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 38: Get annual total for non-existent agent
        runTest("Get annual total for agent ID = 999999", () -> {
            statisticsService.getAnnualTotalForAgent(999999, 2025);
        }, true);
        
        // Test 39: Get department total for non-existent department
        runTest("Get department total for dept ID = 888888", () -> {
            statisticsService.getDepartmentTotal(888888);
        }, true);
        
        // Test 40: Get stats for year = 0
        runTest("Get annual total for year = 0", () -> {
            statisticsService.getAnnualTotalForAgent(1, 0);
        }, false);
        
        // Test 41: Get stats for negative year
        runTest("Get annual total for year = -2025", () -> {
            statisticsService.getAnnualTotalForAgent(1, -2025);
        }, false);
        
        // Test 42: Detect unusual payment with negative threshold
        runTest("Detect unusual payment with threshold = -1000", () -> {
            statisticsService.detectUnusualPayment(-1000);
        }, false);
    }
    
    // ==================== DATABASE CONNECTION FAILURES ====================
    private static void testDatabaseConnectionFailures() {
        System.out.println("\n[TEST CATEGORY 10] Testing Database Connection Issues");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Test 43: Test connection status
        runTest("Verify database connection is active", () -> {
            AgentDAO dao = new AgentDAO();
            dao.findAll();
        }, false);
    }
    
    // ==================== TEST HELPER METHODS ====================
    private static void runTest(String testName, TestRunnable test, boolean shouldFail) {
        totalTests++;
        System.out.print(String.format("Test %d: %-60s", totalTests, testName));
        
        try {
            test.run();
            if (shouldFail) {
                System.out.println(" [❌ FAILED - Expected exception but none thrown]");
                failedTests++;
                crashedTests.add(testName + " - Did not throw expected exception");
            } else {
                System.out.println(" [✅ PASSED]");
                passedTests++;
            }
        } catch (Exception e) {
            if (shouldFail) {
                System.out.println(" [✅ PASSED - Exception caught: " + e.getClass().getSimpleName() + "]");
                passedTests++;
            } else {
                System.out.println(" [❌ FAILED - Unexpected exception: " + e.getMessage() + "]");
                failedTests++;
                crashedTests.add(testName + " - " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
    
    private static void printFinalReport() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    FINAL CRASH TEST REPORT                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\nTotal Tests:  " + totalTests);
        System.out.println("Passed:       " + passedTests + " ✅");
        System.out.println("Failed:       " + failedTests + " ❌");
        System.out.println("Success Rate: " + String.format("%.2f%%", (passedTests * 100.0 / totalTests)));
        
        if (!crashedTests.isEmpty()) {
            System.out.println("\n⚠️  FAILED TESTS:");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            for (int i = 0; i < crashedTests.size(); i++) {
                System.out.println((i + 1) + ". " + crashedTests.get(i));
            }
        } else {
            System.out.println("\n🎉 ALL TESTS PASSED! No crashes detected!");
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Crash test completed successfully!");
        System.out.println("=".repeat(60));
    }
    
    @FunctionalInterface
    interface TestRunnable {
        void run() throws Exception;
    }
}
