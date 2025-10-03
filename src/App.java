package src;

import src.controllers.AgentController;
import src.controllers.AuthController;
import src.controllers.DepartementController;
import src.controllers.PaiementController;
import src.controllers.StatisticsController;
import src.dao.AgentDAO;
import src.dao.DepartementDAO;
import src.dao.PaiementDAO;
import src.services.AgentService;
import src.services.AuthService;
import src.services.DepartementService;
import src.services.PaiementService;
import src.services.SessionService;
import src.services.StatisticsService;
import src.views.AgentMenuView;
import src.views.AuthView;
import src.views.DepartementMenuView;
import src.views.MainMenuView;
import src.views.PaiementMenuView;
import src.views.StatisticsMenuView;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AgentDAO agentDAO = new AgentDAO();
        DepartementDAO departementDAO = new DepartementDAO();
        PaiementDAO paiementDAO = new PaiementDAO();

        SessionService sessionService = new SessionService();
        AuthService authService = new AuthService(agentDAO, sessionService);
        AgentService agentService = new AgentService(agentDAO, departementDAO, paiementDAO);
        DepartementService departementService = new DepartementService(departementDAO, agentDAO, paiementDAO);
        PaiementService paiementService = new PaiementService(paiementDAO, agentDAO);
        StatisticsService statisticsService = new StatisticsService(paiementDAO, agentDAO, departementDAO);

        //init controllers
        AuthController authController = new AuthController(authService);
        AgentController agentController = new AgentController(agentService);
        DepartementController departementController = new DepartementController(departementService);
        PaiementController paiementController = new PaiementController(paiementService);
        StatisticsController statisticsController = new StatisticsController(statisticsService);

        //setup views
        AuthView authView = new AuthView(authController, scanner);
        AgentMenuView agentMenuView = new AgentMenuView(scanner, agentController, departementController);
        DepartementMenuView departementMenuView = new DepartementMenuView(scanner, departementController);
        PaiementMenuView paiementMenuView = new PaiementMenuView(scanner, paiementController, agentController);
        StatisticsMenuView statisticsMenuView = new StatisticsMenuView(scanner, statisticsController);
        MainMenuView mainMenuView = new MainMenuView(scanner, authController, agentMenuView, departementMenuView, paiementMenuView, statisticsMenuView);

        if (authView.showLogin().isPresent()) {
            mainMenuView.start();
        }
    }
}
