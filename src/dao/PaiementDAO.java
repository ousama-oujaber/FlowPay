package src.dao;

import src.dao.interfaces.IPaiement;
import src.models.Paiement;
import src.models.TypePaiement;
import src.config.ConfigDBConn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public class PaiementDAO implements IPaiement {
    
    @Override
    public Paiement save(Paiement paiement) {
        String sql = "INSERT INTO paiement (type, montant, motif, agent_id, condition_validee, date_paiement) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, paiement.getType().name());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setString(3, paiement.getMotif());
            int agentId = paiement.getAgent() != null ? paiement.getAgent().getIdAgent() : paiement.getAgentId();
            if (agentId <= 0) {
                throw new SQLException("Agent inexistant pour le paiement");
            }
            stmt.setInt(4, agentId);
            stmt.setBoolean(5, paiement.isConditionValidee());
            stmt.setDate(6, Date.valueOf(paiement.getDate()));
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paiement.setIdPaiement(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiement;
    }

    @Override
    public void update(Paiement paiement) {
        String sql = "UPDATE paiement SET type = ?, montant = ?, motif = ?, condition_validee = ?, date_paiement = ? WHERE id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paiement.getType().name());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setString(3, paiement.getMotif());
            stmt.setBoolean(4, paiement.isConditionValidee());
            stmt.setDate(5, Date.valueOf(paiement.getDate()));
            stmt.setInt(6, paiement.getIdPaiement());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM paiement WHERE id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Paiement> findById(int id) {
        String sql = "SELECT id, type, montant, date_paiement, motif, agent_id, condition_validee FROM paiement WHERE id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPaiement(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Paiement> findAll() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT id, type, montant, date_paiement, motif, agent_id, condition_validee FROM paiement";
        try (Connection conn = ConfigDBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    @Override
    public List<Paiement> findByAgentId(int agentId) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT id, type, montant, date_paiement, motif, agent_id, condition_validee FROM paiement WHERE agent_id = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paiements.add(mapResultSetToPaiement(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    @Override
    public List<Paiement> findByType(TypePaiement type) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT id, type, montant, date_paiement, motif, agent_id, condition_validee FROM paiement WHERE type = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paiements.add(mapResultSetToPaiement(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    @Override
    public List<Paiement> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT id, type, montant, date_paiement, motif, agent_id, condition_validee FROM paiement WHERE date_paiement BETWEEN ? AND ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paiements.add(mapResultSetToPaiement(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    @Override
    public List<Paiement> findByAgentAndType(int agentId, TypePaiement type) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT id, type, montant, date_paiement, motif, agent_id, condition_validee FROM paiement WHERE agent_id = ? AND type = ?";
        try (Connection conn = ConfigDBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agentId);
            stmt.setString(2, type.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paiements.add(mapResultSetToPaiement(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    private Paiement mapResultSetToPaiement(ResultSet rs) throws SQLException {
        Paiement paiement = new Paiement(
            TypePaiement.valueOf(rs.getString("type")),
            rs.getDouble("montant"),
            rs.getString("motif") != null ? rs.getString("motif") : "",
            null
        );
        paiement.setIdPaiement(rs.getInt("id"));
        Date dateFromDb = rs.getDate("date_paiement");
        if (dateFromDb != null) {
            paiement.setDate(dateFromDb.toLocalDate());
        }
        int agentId = rs.getInt("agent_id");
        if (!rs.wasNull()) {
            paiement.setAgentId(agentId);
        }
        paiement.setConditionValidee(rs.getBoolean("condition_validee"));
        return paiement;
    }
}
