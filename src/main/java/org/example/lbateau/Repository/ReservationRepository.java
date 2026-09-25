package org.example.lbateau.Repository;

import org.example.lbateau.Entity.Reservation;
import org.example.lbateau.Entity.ReservationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByUser_IdOrderByDateCreationDesc(String userId);
    List<Reservation> findByBateau_Id(String bateauId);
    List<Reservation> findByBateau_IdAndStatutIn(String bateauId, Collection<ReservationStatus> statuses);
    List<Reservation> findByStatut(ReservationStatus statut);
}
