package src.dao.interfaces;

import src.models.Agent;
import java.util.List;
import java.util.Optional;

public interface IAgent {
    Agent save(Agent agent);
    void update(Agent agent);
    void deleteById(int id);
    Optional<Agent> findById(int id);
    List<Agent> findAll();
    Optional<Agent> findByEmail(String email);
    List<Agent> findByDepartementId(int departementId);
    List<Agent> findByTypeAgent(String typeAgent);
}
