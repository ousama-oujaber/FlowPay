package src.services.interfaces;

import src.exceptions.AuthenticationException;
import src.models.Agent;

import java.util.Optional;

public interface IAuthService {
    
    Agent login(String email, String password) throws AuthenticationException;
    
    void logout();
    
    boolean isAuthenticated();
    
    Optional<Agent> getCurrentAgent();
}
