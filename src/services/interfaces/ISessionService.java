package src.services.interfaces;

import src.models.Agent;
import src.models.Session;

import java.util.Optional;

public interface ISessionService {
    
    void startSession(Agent agent);
    
    void endSession();
    
    boolean isAuthenticated();
    
    Optional<Session> getCurrentSession();
    
    Optional<Agent> getCurrentAgent();
}
