package src.test;

import src.dao.*;
import src.models.*;
import src.services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Stress Test Suite - Tests system behavior under heavy load
 * Tests performance, memory usage, and stability under pressure
 */
public class StressTest {
    
    private static final Random random = new Random();
    private static int successfulOperations = 0;
    private static int failedOperations = 0;
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         FlowPay STRESS TEST SUITE                         ║");
        System.out.println("║         Testing System Under Heavy Load                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Initialize services
        AgentDAO agentDAO = new AgentDAO();
        DepartementDAO departementDAO = new DepartementDAO();
        PaiementDAO paiementDAO = new PaiementDAO();
        
        AgentService agentService = new AgentService(agentDAO, departementDAO, paiementDAO);
        DepartementService departementService = new DepartementService(departementDAO, agentDAO, paiementDAO);
        PaiementService paiementService = new PaiementService(paiementDAO, agentDAO);
        StatisticsService statisticsService = new StatisticsService(paiementDAO, agentDAO, departementDAO);
        
        // Run stress tests
        testRapidReadOperations(agentDAO, departementDAO, paiementDAO);
        testConcurrentReads(agentDAO, departementDAO, paiementDAO);
        testLargeResultSets(statisticsService);
        testMemoryUsage(agentDAO, departementDAO, paiementDAO);
        testLongRunningOperations(agentDAO);
        
        // Print final report
        printFinalReport();
    }
    
    // ==================== RAPID READ OPERATIONS ====================
    private static void testRapidReadOperations(AgentDAO agentDAO, DepartementDAO departementDAO, PaiementDAO paiementDAO) {
        System.out.println("\n[STRESS TEST 1] Rapid Sequential Read Operations");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        long startTime = System.currentTimeMillis();
        int iterations = 1000;
        
        System.out.println("Performing " + iterations + " rapid read operations...");
        
        for (int i = 0; i < iterations; i++) {
            try {
                agentDAO.findAll();
                departementDAO.findAll();
                paiementDAO.findAll();
                successfulOperations += 3;
            } catch (Exception e) {
                failedOperations += 3;
                System.out.println("Error at iteration " + i + ": " + e.getMessage());
            }
            
            if ((i + 1) % 200 == 0) {
                System.out.println("  Progress: " + (i + 1) + "/" + iterations + " iterations");
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("✅ Completed " + iterations + " iterations in " + duration + "ms");
        System.out.println("   Average: " + String.format("%.2f", duration / (double)iterations) + "ms per iteration");
        System.out.println("   Operations/second: " + String.format("%.2f", (iterations * 3 * 1000.0) / duration));
    }
    
    // ==================== CONCURRENT READS ====================
    private static void testConcurrentReads(AgentDAO agentDAO, DepartementDAO departementDAO, PaiementDAO paiementDAO) {
        System.out.println("\n[STRESS TEST 2] Concurrent Database Access");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        int threadCount = 50;
        int operationsPerThread = 20;
        
        System.out.println("Launching " + threadCount + " threads, " + operationsPerThread + " ops each...");
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<?>> futures = new ArrayList<>();
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            futures.add(executor.submit(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    try {
                        // Random read operation
                        int operation = random.nextInt(3);
                        switch (operation) {
                            case 0:
                                agentDAO.findAll();
                                break;
                            case 1:
                                departementDAO.findAll();
                                break;
                            case 2:
                                paiementDAO.findAll();
                                break;
                        }
                        successfulOperations++;
                    } catch (Exception e) {
                        failedOperations++;
                        System.out.println("Thread " + threadId + " error: " + e.getMessage());
                    }
                }
            }));
        }
        
        // Wait for all threads
        for (Future<?> future : futures) {
            try {
                future.get(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.out.println("Thread timeout: " + e.getMessage());
                failedOperations++;
            }
        }
        
        executor.shutdown();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("✅ Completed " + threadCount + " threads in " + duration + "ms");
        System.out.println("   Total operations: " + (threadCount * operationsPerThread));
        System.out.println("   Average: " + String.format("%.2f", duration / (double)(threadCount * operationsPerThread)) + "ms per operation");
    }
    
    // ==================== LARGE RESULT SETS ====================
    private static void testLargeResultSets(StatisticsService statisticsService) {
        System.out.println("\n[STRESS TEST 3] Large Result Set Processing");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        long startTime = System.currentTimeMillis();
        
        try {
            System.out.println("Calculating global statistics...");
            
            double globalTotal = statisticsService.getGlobalPaymentsTotal();
            System.out.println("  Global payments total: " + globalTotal);
            
            var distribution = statisticsService.getPaymentDistribution();
            System.out.println("  Payment types: " + distribution.size());
            
            var rankedAgents = statisticsService.rankAgentsByTotalPayments();
            System.out.println("  Ranked agents: " + rankedAgents.size());
            
            long totalAgents = statisticsService.getTotalAgents();
            System.out.println("  Total agents: " + totalAgents);
            
            long totalDepts = statisticsService.getTotalDepartements();
            System.out.println("  Total departments: " + totalDepts);
            
            successfulOperations += 5;
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            failedOperations++;
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("✅ Completed in " + (endTime - startTime) + "ms");
    }
    
    // ==================== MEMORY USAGE TEST ====================
    private static void testMemoryUsage(AgentDAO agentDAO, DepartementDAO departementDAO, PaiementDAO paiementDAO) {
        System.out.println("\n[STRESS TEST 4] Memory Usage Under Load");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        Runtime runtime = Runtime.getRuntime();
        
        // Force garbage collection
        System.gc();
        Thread.yield();
        
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before test: " + formatBytes(memoryBefore));
        
        // Perform many operations
        int iterations = 500;
        System.out.println("Performing " + iterations + " operations...");
        
        for (int i = 0; i < iterations; i++) {
            try {
                agentDAO.findAll();
                departementDAO.findAll();
                paiementDAO.findAll();
                successfulOperations += 3;
            } catch (Exception e) {
                failedOperations++;
            }
        }
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after test:  " + formatBytes(memoryAfter));
        System.out.println("Memory used:        " + formatBytes(memoryAfter - memoryBefore));
        System.out.println("Total memory:       " + formatBytes(runtime.totalMemory()));
        System.out.println("Max memory:         " + formatBytes(runtime.maxMemory()));
        System.out.println("Free memory:        " + formatBytes(runtime.freeMemory()));
        
        System.out.println("✅ Memory test completed");
    }
    
    // ==================== LONG RUNNING OPERATIONS ====================
    private static void testLongRunningOperations(AgentDAO agentDAO) {
        System.out.println("\n[STRESS TEST 5] Long Running Operations");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        System.out.println("Testing sustained load for 10 seconds...");
        
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 10000; // 10 seconds
        int operationCount = 0;
        
        while (System.currentTimeMillis() < endTime) {
            try {
                agentDAO.findAll();
                operationCount++;
                successfulOperations++;
                
                // Small delay to prevent overwhelming the system
                Thread.sleep(10);
            } catch (Exception e) {
                failedOperations++;
            }
        }
        
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("✅ Completed " + operationCount + " operations");
        System.out.println("   Duration: " + duration + "ms");
        System.out.println("   Average: " + String.format("%.2f", (double)operationCount / (duration / 1000.0)) + " ops/second");
    }
    
    // ==================== HELPER METHODS ====================
    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    private static void printFinalReport() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  FINAL STRESS TEST REPORT                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        
        int totalOperations = successfulOperations + failedOperations;
        double successRate = totalOperations > 0 ? (successfulOperations * 100.0 / totalOperations) : 0;
        
        System.out.println("\nTotal Operations:    " + totalOperations);
        System.out.println("Successful:          " + successfulOperations + " ✅");
        System.out.println("Failed:              " + failedOperations + " ❌");
        System.out.println("Success Rate:        " + String.format("%.2f%%", successRate));
        
        if (failedOperations == 0) {
            System.out.println("\n🎉 ALL STRESS TESTS PASSED! System is stable under load!");
        } else {
            System.out.println("\n⚠️  Some operations failed under stress conditions.");
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Stress test completed successfully!");
        System.out.println("=".repeat(60));
    }
}
