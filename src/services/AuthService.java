package src.services;

import src.dao.AgentDAO;
import src.exceptions.AuthenticationException;
import src.models.Agent;
import src.models.Session;
import src.services.interfaces.IAuthService;

import java.util.Optional;

public class AuthService implements IAuthService {
    private final AgentDAO agentDAO;
    private final SessionService sessionService;

    public AuthService(AgentDAO agentDAO, SessionService sessionService) {
        this.agentDAO = agentDAO;
        this.sessionService = sessionService;
    }

    public Agent login(String email, String password) throws AuthenticationException {
        Optional<Agent> agentOptional = agentDAO.findByEmail(email);

        if (agentOptional.isEmpty()) {
            throw new AuthenticationException("Invalid email or password");
        }

        Agent agent = agentOptional.get();

        if (!agent.getMotDePasse().equals(password)) {
            throw new AuthenticationException("Invalid email or password");
        }

        sessionService.startSession(agent);
        return agent;
    }

    public void logout() {
        sessionService.endSession();
    }

    public boolean isAuthenticated() {
        return sessionService.isAuthenticated();
    }

    public Optional<Agent> getCurrentAgent() {
        return sessionService.getCurrentAgent();
    }
}
