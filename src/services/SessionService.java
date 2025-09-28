package src.services;

import src.models.Agent;
import src.models.Session;
import src.services.interfaces.ISessionService;

import java.util.Optional;

public class SessionService implements ISessionService {
    private Session currentSession;

    public void startSession(Agent agent) {
        currentSession = new Session(agent);
    }

    public void endSession() {
        currentSession = null;
    }

    public boolean isAuthenticated() {
        return currentSession != null;
    }

    public Optional<Session> getCurrentSession() {
        return Optional.ofNullable(currentSession);
    }

    public Optional<Agent> getCurrentAgent() {
        return getCurrentSession().map(Session::getAgent);
    }
}
