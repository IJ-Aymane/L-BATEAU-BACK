package org.example.lbateau.Repository;


import org.example.lbateau.Entity.Bateau;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BateauRepository extends MongoRepository<Bateau, String> {
    // Spring ghadi ysawb les requêtes lrasso!
    // Matalan, ila bghiti tjbd ghir l'bateaux li disponibles:
    List<Bateau> findByDisponibleTrue();
}