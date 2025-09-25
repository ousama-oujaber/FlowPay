package src.dao.interfaces;

import src.models.Paiement;
import src.models.TypePaiement;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface IPaiement {
    Paiement save(Paiement paiement);
    void update(Paiement paiement);
    void deleteById(int id);
    Optional<Paiement> findById(int id);
    List<Paiement> findAll();
    List<Paiement> findByAgentId(int agentId);
    List<Paiement> findByType(TypePaiement type);
    List<Paiement> findByDateRange(LocalDate startDate, LocalDate endDate);
    List<Paiement> findByAgentAndType(int agentId, TypePaiement type);
}