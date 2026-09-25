package org.example.lbateau.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.example.lbateau.Entity.Bateau;
import org.example.lbateau.Entity.Reservation;
import org.example.lbateau.Entity.ReservationStatus;
import org.example.lbateau.Entity.User;

import java.time.LocalDateTime;

public class ReservationDTOs {
    public static class UserOption {
        private final String id;
        private final String username;

        public UserOption(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }

        public String getId() { return id; }
        public String getUsername() { return username; }
    }

    public static class CreateReservationRequest {
        private String userId;
        private String bateauId;
        private LocalDateTime dateDebut;

        @JsonAlias({"nbHeures", "duration"})
        private int nombreHeures;

        @JsonAlias({"avance", "montantPaye"})
        private double montantAvance;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getBateauId() { return bateauId; }
        public void setBateauId(String bateauId) { this.bateauId = bateauId; }
        public LocalDateTime getDateDebut() { return dateDebut; }
        public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }
        public int getNombreHeures() { return nombreHeures; }
        public void setNombreHeures(int nombreHeures) { this.nombreHeures = nombreHeures; }
        public double getMontantAvance() { return montantAvance; }
        public void setMontantAvance(double montantAvance) { this.montantAvance = montantAvance; }
    }

    public static class ReservationResponse {
        private final String id;
        private final String userId;
        private final String clientId;
        private final String username;
        private final String clientName;
        private final String userEmail;
        private final String userTelephone;
        private final String bateauId;
        private final String bateauNom;
        private final String bateauType;
        private final LocalDateTime dateDebut;
        private final int nombreHeures;
        private final int nbHeures;
        private final LocalDateTime dateFin;
        private final double prixHT;
        private final double tva;
        private final double prixTotal;
        private final double montantTotal;
        private final double montantAvance;
        private final double montantPaye;
        private final double montantRestant;
        private final String statut;
        private final LocalDateTime dateCreation;

        public ReservationResponse(Reservation reservation) {
            User user = reservation.getUser();
            Bateau bateau = reservation.getBateau();
            ReservationStatus normalized = reservation.getStatut() != null ? reservation.getStatut().normalized() : ReservationStatus.PENDING;
            this.id = reservation.getId();
            this.userId = user != null ? user.getId() : null;
            this.clientId = this.userId;
            this.username = user != null ? user.getUsername() : null;
            this.clientName = this.username;
            this.userEmail = user != null ? user.getEmail() : null;
            this.userTelephone = user != null ? user.getTelephone() : null;
            this.bateauId = bateau != null ? bateau.getId() : null;
            this.bateauNom = bateau != null ? bateau.getNom() : null;
            this.bateauType = bateau != null ? bateau.getType() : null;
            this.dateDebut = reservation.getDateDebut();
            this.nombreHeures = reservation.getNombreHeures();
            this.nbHeures = reservation.getNombreHeures();
            this.dateFin = reservation.getDateFin();
            this.prixHT = reservation.getPrixHT();
            this.tva = reservation.getTva();
            this.prixTotal = reservation.getPrixTotal();
            this.montantTotal = reservation.getPrixTotal();
            this.montantAvance = reservation.getMontantAvance();
            this.montantPaye = reservation.getMontantAvance();
            this.montantRestant = reservation.getMontantRestant();
            this.statut = normalized.name();
            this.dateCreation = reservation.getDateCreation();
        }

        public String getId() { return id; }
        public String getUserId() { return userId; }
        public String getClientId() { return clientId; }
        public String getUsername() { return username; }
        public String getClientName() { return clientName; }
        public String getUserEmail() { return userEmail; }
        public String getUserTelephone() { return userTelephone; }
        public String getBateauId() { return bateauId; }
        public String getBateauNom() { return bateauNom; }
        public String getBateauType() { return bateauType; }
        public LocalDateTime getDateDebut() { return dateDebut; }
        public int getNombreHeures() { return nombreHeures; }
        public int getNbHeures() { return nbHeures; }
        public LocalDateTime getDateFin() { return dateFin; }
        public double getPrixHT() { return prixHT; }
        public double getTva() { return tva; }
        public double getPrixTotal() { return prixTotal; }
        public double getMontantTotal() { return montantTotal; }
        public double getMontantAvance() { return montantAvance; }
        public double getMontantPaye() { return montantPaye; }
        public double getMontantRestant() { return montantRestant; }
        public String getStatut() { return statut; }
        public LocalDateTime getDateCreation() { return dateCreation; }
    }

    public static class InvoiceResponse {
        private final String reservationId;
        private final String reference;
        private final ClientInfo client;
        private final BoatInfo bateau;
        private final LocalDateTime dateDebut;
        private final LocalDateTime dateFin;
        private final int nombreHeures;
        private final double prixParHeure;
        private final double subtotal;
        private final double tva;
        private final double totalPrice;
        private final double montantAvance;
        private final double montantRestant;
        private final String paymentStatus;
        private final String statut;
        private final LocalDateTime dateCreation;

        public InvoiceResponse(Reservation reservation) {
            Bateau boat = reservation.getBateau();
            ReservationStatus normalized = reservation.getStatut() != null ? reservation.getStatut().normalized() : ReservationStatus.PENDING;
            this.reservationId = reservation.getId();
            this.reference = "FAC-" + (reservation.getId() != null && reservation.getId().length() > 8 ? reservation.getId().substring(reservation.getId().length() - 8).toUpperCase() : reservation.getId());
            this.client = new ClientInfo(reservation.getUser());
            this.bateau = new BoatInfo(boat);
            this.dateDebut = reservation.getDateDebut();
            this.dateFin = reservation.getDateFin();
            this.nombreHeures = reservation.getNombreHeures();
            this.prixParHeure = boat != null ? boat.getPrixParHeure() : 0;
            this.subtotal = reservation.getPrixHT();
            this.tva = reservation.getTva();
            this.totalPrice = reservation.getPrixTotal();
            this.montantAvance = reservation.getMontantAvance();
            this.montantRestant = reservation.getMontantRestant();
            this.paymentStatus = reservation.getMontantRestant() <= 0 ? "PAID" : (reservation.getMontantAvance() > 0 ? "PARTIAL" : "PENDING");
            this.statut = normalized.name();
            this.dateCreation = reservation.getDateCreation();
        }

        public String getReservationId() { return reservationId; }
        public String getReference() { return reference; }
        public ClientInfo getClient() { return client; }
        public BoatInfo getBateau() { return bateau; }
        public LocalDateTime getDateDebut() { return dateDebut; }
        public LocalDateTime getDateFin() { return dateFin; }
        public int getNombreHeures() { return nombreHeures; }
        public double getPrixParHeure() { return prixParHeure; }
        public double getSubtotal() { return subtotal; }
        public double getTva() { return tva; }
        public double getTotalPrice() { return totalPrice; }
        public double getMontantAvance() { return montantAvance; }
        public double getMontantRestant() { return montantRestant; }
        public String getPaymentStatus() { return paymentStatus; }
        public String getStatut() { return statut; }
        public LocalDateTime getDateCreation() { return dateCreation; }
    }

    public static class ClientInfo {
        private final String id;
        private final String username;
        private final String email;
        private final String telephone;

        public ClientInfo(User user) {
            this.id = user != null ? user.getId() : null;
            this.username = user != null ? user.getUsername() : null;
            this.email = user != null ? user.getEmail() : null;
            this.telephone = user != null ? user.getTelephone() : null;
        }

        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getTelephone() { return telephone; }
    }

    public static class BoatInfo {
        private final String id;
        private final String nom;
        private final String type;
        private final String marque;
        private final double prixParHeure;

        public BoatInfo(Bateau bateau) {
            this.id = bateau != null ? bateau.getId() : null;
            this.nom = bateau != null ? bateau.getNom() : null;
            this.type = bateau != null ? bateau.getType() : null;
            this.marque = bateau != null ? bateau.getMarque() : null;
            this.prixParHeure = bateau != null ? bateau.getPrixParHeure() : 0;
        }

        public String getId() { return id; }
        public String getNom() { return nom; }
        public String getType() { return type; }
        public String getMarque() { return marque; }
        public double getPrixParHeure() { return prixParHeure; }
    }
}
