package src.dao;

import src.dao.interfaces.IAgent;
import src.models.Agent;
import src.models.Departement;
import src.models.TypeAgent;
import src.config.ConfigDBConn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgentDAO implements IAgent {
    
    @Override
    public Agent save(Agent agent) {
        String sql = "INSERT INTO agent (nom, prenom, email, mot_de_passe, type_agent, departement_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, agent.getNom());
            stmt.setString(2, agent.getPrenom());
            stmt.setString(3, agent.getEmail());
            stmt.setString(4, agent.getMotDePasse());
            stmt.setString(5, agent.getTypeAgent().name());
            if (agent.getDepartement() != null) {
                stmt.setInt(6, agent.getDepartement().getIdDepartement());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    agent.setIdAgent(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agent;
    }

    @Override
    public void update(Agent agent) {
        String sql = "UPDATE agent SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, type_agent = ?, departement_id = ? WHERE id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, agent.getNom());
            stmt.setString(2, agent.getPrenom());
            stmt.setString(3, agent.getEmail());
            stmt.setString(4, agent.getMotDePasse());
            stmt.setString(5, agent.getTypeAgent().name());
            if (agent.getDepartement() != null) {
                stmt.setInt(6, agent.getDepartement().getIdDepartement());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setInt(7, agent.getIdAgent());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM agent WHERE id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Agent> findById(int id) {
    String sql = "SELECT a.id, a.nom, a.prenom, a.email, a.mot_de_passe, a.type_agent, " +
        "a.departement_id, d.nom AS departement_nom " +
        "FROM agent a LEFT JOIN departement d ON a.departement_id = d.id WHERE a.id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAgent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Agent> findAll() {
        List<Agent> agents = new ArrayList<>();
    String sql = "SELECT a.id, a.nom, a.prenom, a.email, a.mot_de_passe, a.type_agent, " +
        "a.departement_id, d.nom AS departement_nom " +
        "FROM agent a LEFT JOIN departement d ON a.departement_id = d.id";
        try (Connection conn = ConfigDBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                agents.add(mapResultSetToAgent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    @Override
    public Optional<Agent> findByEmail(String email) {
    String sql = "SELECT a.id, a.nom, a.prenom, a.email, a.mot_de_passe, a.type_agent, " +
        "a.departement_id, d.nom AS departement_nom " +
        "FROM agent a LEFT JOIN departement d ON a.departement_id = d.id WHERE a.email = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAgent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Agent> findByDepartementId(int departementId) {
        List<Agent> agents = new ArrayList<>();
    String sql = "SELECT a.id, a.nom, a.prenom, a.email, a.mot_de_passe, a.type_agent, " +
        "a.departement_id, d.nom AS departement_nom " +
        "FROM agent a LEFT JOIN departement d ON a.departement_id = d.id WHERE a.departement_id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departementId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    agents.add(mapResultSetToAgent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    @Override
    public List<Agent> findByTypeAgent(String typeAgent) {
        List<Agent> agents = new ArrayList<>();
    String sql = "SELECT a.id, a.nom, a.prenom, a.email, a.mot_de_passe, a.type_agent, " +
        "a.departement_id, d.nom AS departement_nom " +
        "FROM agent a LEFT JOIN departement d ON a.departement_id = d.id WHERE a.type_agent = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, typeAgent);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    agents.add(mapResultSetToAgent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    private Agent mapResultSetToAgent(ResultSet rs) throws SQLException {
        Agent agent = new Agent(
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("mot_de_passe"),
                TypeAgent.valueOf(rs.getString("type_agent"))
        );
        agent.setIdAgent(rs.getInt("id"));
        int departementId = rs.getInt("departement_id");
        if (!rs.wasNull() && departementId > 0) {
            String departementNom = rs.getString("departement_nom");
            Departement departement = new Departement(departementNom != null ? departementNom : "");
            departement.setIdDepartement(departementId);
            agent.setDepartement(departement);
        }
        return agent;
    }
}
