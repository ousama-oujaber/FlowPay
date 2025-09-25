package src.dao.interfaces;

import src.models.Departement;
import java.util.List;
import java.util.Optional;

public interface IDepartement {
    Departement save(Departement departement);
    void update(Departement departement);
    void deleteById(int id);
    Optional<Departement> findById(int id);
    List<Departement> findAll();
    Optional<Departement> findByNom(String nom);
}