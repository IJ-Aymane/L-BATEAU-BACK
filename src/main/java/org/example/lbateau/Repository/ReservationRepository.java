package org.example.lbateau.Repository;


import org.example.lbateau.Entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    // Tjbed ga3 les réservations li darhom wa7d l'client
    List<Reservation> findByClientId(String clientId);

    // Tjbed ga3 les réservations li tدارo 3la wa7d l'bateau
    List<Reservation> findByBateauId(String bateauId);

    // Tjbed les réservations 3la 7sab l'état dialhom (ex: "EN_ATTENTE", "CONFIRMEE")
    List<Reservation> findByStatut(String statut);
}
