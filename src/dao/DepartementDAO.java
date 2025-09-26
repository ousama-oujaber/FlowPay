package src.dao;

import src.dao.interfaces.IDepartement;
import src.models.Departement;
import src.models.Agent;
import src.config.ConfigDBConn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartementDAO implements IDepartement {
    
    @Override
    public Departement save(Departement departement) {
        String sql = "INSERT INTO departement (nom, responsable_id) VALUES (?, ?)";

        try (Connection conn = ConfigDBConn.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, departement.getNom());

            if(departement.getResponsable() != null) {
                stmt.setInt(2, departement.getResponsable().getIdAgent());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet generatedKey = stmt.getGeneratedKeys()) {
                if (generatedKey.next()){
                    departement.setIdDepartement(generatedKey.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departement;
    }


    @Override
    public void update(Departement departement) {
        String sql = "UPDATE departement SET nom = ?, responsable_id = ? WHERE id = ?";

        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, departement.getNom());
            if (departement.getResponsable() != null) {
                stmt.setInt(2, departement.getResponsable().getIdAgent());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, departement.getIdDepartement());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM departement WHERE id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Departement> findById(int id) {
        String sql = "SELECT id, nom, responsable_id FROM departement WHERE id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDepartement(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Departement> findAll() {
        List<Departement> departements = new ArrayList<>();
        String sql = "SELECT id, nom, responsable_id FROM departement";
        try (Connection conn = ConfigDBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departements.add(mapResultSetToDepartement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departements;
    }

    @Override
    public Optional<Departement> findByNom(String nom) {
        String sql = "SELECT id, nom, responsable_id FROM departement WHERE nom = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDepartement(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Departement findByIdWithAgents(int id) {
        String sql = "SELECT d.id, d.nom, d.responsable_id, " +
                    "a.id as agent_id, a.nom as agent_nom, a.prenom as agent_prenom, " +
                    "a.email as agent_email, a.type_agent " +
                    "FROM departement d " +
                    "LEFT JOIN agent a ON d.id = a.departement_id " +
                    "WHERE d.id = ?";
        
        Departement departement = null;
        List<Agent> agents = new ArrayList<>();
        
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (departement == null) {
                        departement = new Departement(rs.getString("nom"));
                        departement.setIdDepartement(rs.getInt("id"));
                    }
                    
                    if (rs.getInt("agent_id") > 0) {
                        Agent agent = new Agent(
                            rs.getString("agent_nom"),
                            rs.getString("agent_prenom"),
                            rs.getString("agent_email"),
                            "",
                            src.models.TypeAgent.valueOf(rs.getString("type_agent"))
                        );
                        agent.setIdAgent(rs.getInt("agent_id"));
                        agents.add(agent);
                    }
                }
                
                if (departement != null) {
                    departement.setAgents(agents);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departement;
    }

    private Departement mapResultSetToDepartement(ResultSet rs) throws SQLException {
        Departement departement = new Departement(rs.getString("nom"));
        departement.setIdDepartement(rs.getInt("id"));
        return departement;
    }
}
