package org.example.lbateau.Entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "reservations")
@TypeAlias("org.example.lbateau.Entity.Reservation")
public class Reservation {
    @Id
    private String id;

    @DBRef
    @JsonIgnore
    private User user;

    @DBRef
    @JsonIgnore
    private Bateau bateau;

    private LocalDateTime dateDebut;

    @JsonAlias({"nbHeures", "duration"})
    private int nombreHeures;

    private LocalDateTime dateFin;
    private double prixTotal;
    private ReservationStatus statut = ReservationStatus.PENDING;
    private LocalDateTime dateCreation;

    @JsonProperty("userId")
    public String getUserId() {
        return user != null ? user.getId() : null;
    }

    @JsonProperty("clientId")
    public String getClientId() {
        return getUserId();
    }

    @JsonProperty("bateauId")
    public String getBateauId() {
        return bateau != null ? bateau.getId() : null;
    }

    @JsonProperty("bateauNom")
    public String getBateauNom() {
        return bateau != null ? bateau.getNom() : null;
    }

    @JsonProperty("nbHeures")
    public int getNbHeures() {
        return nombreHeures;
    }

    @JsonProperty("montantTotal")
    public double getMontantTotal() {
        return prixTotal;
    }

    @JsonProperty("montantPaye")
    public double getMontantPaye() {
        return statut != null && statut.normalized() == ReservationStatus.CONFIRMED ? prixTotal : 0;
    }

    @JsonProperty("montantRestant")
    public double getMontantRestant() {
        return prixTotal - getMontantPaye();
    }
}
