package org.example.lbateau.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Document(collection = "reservations")
public class Reservation {
    @Id



    private String id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private double nombreHeures;
    private int nombrePersonnes;
    private double montantTotal;
    private double montantPaye;
    private double montantRestant;
    private String statut; // Ex: EN_ATTENTE, CONFIRMEE, ANNULEE

    // F MongoDB, n9drou nkhabbiw ghir l'ID dial Bateau w Client (Référence manuelle)
    private String bateauId;
    private String clientId;

    private Date dateCreation;
}
