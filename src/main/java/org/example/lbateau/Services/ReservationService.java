package org.example.lbateau.Services;

import org.example.lbateau.Entity.Reservation;
import org.example.lbateau.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservationsByClientId(String clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public Reservation createReservation(Reservation reservation) {
        reservation.setDateCreation(new Date());
        if(reservation.getStatut() == null) {
            reservation.setStatut("EN_ATTENTE"); // Statut par défaut
        }
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(String id, Reservation resDetails) {
        Optional<Reservation> resExistante = reservationRepository.findById(id);
        if (resExistante.isPresent()) {
            Reservation reservation = resExistante.get();
            reservation.setDateCreation(resDetails.getDateCreation());
            reservation.setDateDebut(resDetails.getDateDebut());
            reservation.setDateFin(resDetails.getDateFin());
            reservation.setNombreHeures(resDetails.getNombreHeures());
            reservation.setNombrePersonnes(resDetails.getNombrePersonnes());
            reservation.setMontantTotal(resDetails.getMontantTotal());
            reservation.setMontantPaye(resDetails.getMontantPaye());
            reservation.setMontantRestant(resDetails.getMontantRestant());
            reservation.setStatut(resDetails.getStatut());
            reservation.setBateauId(resDetails.getBateauId());
            reservation.setClientId(resDetails.getClientId());
            return reservationRepository.save(reservation);
        }
        return null;
    }

    public void deleteReservation(String id) {
        reservationRepository.deleteById(id);
    }
}
