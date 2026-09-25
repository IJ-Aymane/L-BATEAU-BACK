package org.example.lbateau.Services;

import org.example.lbateau.DTO.ReservationDTOs;
import org.example.lbateau.Entity.Bateau;
import org.example.lbateau.Entity.Reservation;
import org.example.lbateau.Entity.ReservationStatus;
import org.example.lbateau.Entity.User;
import org.example.lbateau.Repository.BateauRepository;
import org.example.lbateau.Repository.ReservationRepository;
import org.example.lbateau.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BateauRepository bateauRepository;

    private static final double TVA_RATE = 0.20;

    private static final List<ReservationStatus> ACTIVE_STATUSES = List.of(
            ReservationStatus.PENDING,
            ReservationStatus.CONFIRMED,
            ReservationStatus.EN_ATTENTE,
            ReservationStatus.CONFIRMEE
    );

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<ReservationDTOs.UserOption> listReservableUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles() != null && user.getRoles().stream().anyMatch(role -> "ROLE_CLIENT".equalsIgnoreCase(role)))
                .map(ReservationDTOs.UserOption::new)
                .toList();
    }

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public Reservation getReservationOrThrow(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    public List<Reservation> getReservationsByUserId(String userId) {
        validateId(userId, "User id is required");
        return reservationRepository.findByUser_IdOrderByDateCreationDesc(userId);
    }

    public List<Reservation> getReservationsByClientId(String clientId) {
        return getReservationsByUserId(clientId);
    }

    public Reservation createReservation(ReservationDTOs.CreateReservationRequest request) {
        validateCreateRequest(request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Bateau bateau = bateauRepository.findById(request.getBateauId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bateau not found"));

        if (!bateau.isDisponible()) {
            throw new IllegalStateException("Ce bateau n'est pas disponible pour la réservation.");
        }

        LocalDateTime dateDebut = request.getDateDebut();
        LocalDateTime dateFin = dateDebut.plusHours(request.getNombreHeures());
        ensureSlotAvailable(bateau.getId(), dateDebut, dateFin, null);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBateau(bateau);
        reservation.setDateDebut(dateDebut);
        reservation.setNombreHeures(request.getNombreHeures());
        reservation.setDateFin(dateFin);
        applyPricing(reservation, bateau, request.getMontantAvance());
        reservation.setStatut(ReservationStatus.PENDING);
        reservation.setDateCreation(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public Reservation createReservation(Reservation reservation) {
        if (reservation.getUser() == null || reservation.getUser().getId() == null) {
            throw new IllegalArgumentException("userId is required");
        }
        if (reservation.getBateau() == null || reservation.getBateau().getId() == null) {
            throw new IllegalArgumentException("bateauId is required");
        }
        ReservationDTOs.CreateReservationRequest request = new ReservationDTOs.CreateReservationRequest();
        request.setUserId(reservation.getUser().getId());
        request.setBateauId(reservation.getBateau().getId());
        request.setDateDebut(reservation.getDateDebut());
        request.setNombreHeures(reservation.getNombreHeures());
        request.setMontantAvance(reservation.getMontantAvance());
        return createReservation(request);
    }

    public Reservation updateReservation(String id, Reservation resDetails) {
        Reservation reservation = getReservationOrThrow(id);

        if (resDetails.getDateDebut() != null) {
            reservation.setDateDebut(resDetails.getDateDebut());
        }
        if (resDetails.getNombreHeures() > 0) {
            reservation.setNombreHeures(resDetails.getNombreHeures());
        }
        if (resDetails.getStatut() != null) {
            reservation.setStatut(resDetails.getStatut().normalized());
        }
        if (resDetails.getBateau() != null && resDetails.getBateau().getId() != null) {
            Bateau bateau = bateauRepository.findById(resDetails.getBateau().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bateau not found"));
            reservation.setBateau(bateau);
        }
        if (resDetails.getUser() != null && resDetails.getUser().getId() != null) {
            User user = userRepository.findById(resDetails.getUser().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            reservation.setUser(user);
        }

        if (reservation.getDateDebut() != null && reservation.getNombreHeures() > 0) {
            LocalDateTime dateFin = reservation.getDateDebut().plusHours(reservation.getNombreHeures());
            reservation.setDateFin(dateFin);
            if (reservation.getBateau() != null) {
                ensureSlotAvailable(reservation.getBateau().getId(), reservation.getDateDebut(), dateFin, reservation.getId());
                applyPricing(reservation, reservation.getBateau(), reservation.getMontantAvance());
            }
        }

        return reservationRepository.save(reservation);
    }

    public ReservationDTOs.ReservationResponse toResponse(Reservation reservation) {
        return new ReservationDTOs.ReservationResponse(reservation);
    }

    public ReservationDTOs.InvoiceResponse buildInvoice(String id) {
        return new ReservationDTOs.InvoiceResponse(getReservationOrThrow(id));
    }

    public void deleteReservation(String id) {
        reservationRepository.deleteById(id);
    }

    private void applyPricing(Reservation reservation, Bateau bateau, double requestedAdvance) {
        double prixHT = bateau.getPrixParHeure() * reservation.getNombreHeures();
        double tva = prixHT * TVA_RATE;
        double prixTotal = prixHT + tva;
        double avance = Math.max(0, Math.min(requestedAdvance, prixTotal));
        reservation.setPrixHT(prixHT);
        reservation.setTva(tva);
        reservation.setPrixTotal(prixTotal);
        reservation.setMontantAvance(avance);
        reservation.setMontantRestant(prixTotal - avance);
    }

    private void validateCreateRequest(ReservationDTOs.CreateReservationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Reservation request is required");
        }
        validateId(request.getUserId(), "User id is required");
        validateId(request.getBateauId(), "Bateau id is required");
        if (request.getDateDebut() == null) {
            throw new IllegalArgumentException("dateDebut is required");
        }
        if (request.getNombreHeures() <= 0) {
            throw new IllegalArgumentException("nombreHeures must be greater than 0");
        }
    }

    private void validateId(String id, String message) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void ensureSlotAvailable(String bateauId, LocalDateTime requestedStart, LocalDateTime requestedEnd, String ignoredReservationId) {
        boolean hasOverlap = reservationRepository.findByBateau_IdAndStatutIn(bateauId, ACTIVE_STATUSES).stream()
                .filter(existing -> ignoredReservationId == null || !ignoredReservationId.equals(existing.getId()))
                .filter(existing -> existing.getDateDebut() != null && existing.getDateFin() != null)
                .anyMatch(existing -> overlaps(requestedStart, requestedEnd, existing.getDateDebut(), existing.getDateFin()));

        if (hasOverlap) {
            throw new IllegalStateException("Ce bateau est déjà réservé sur ce créneau.");
        }
    }

    private boolean overlaps(LocalDateTime requestedStart, LocalDateTime requestedEnd, LocalDateTime existingStart, LocalDateTime existingEnd) {
        return requestedStart.isBefore(existingEnd) && requestedEnd.isAfter(existingStart);
    }
}
