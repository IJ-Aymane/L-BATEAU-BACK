package org.example.lbateau.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data // Katgénérer les getters/setters auto
@Document(collection = "bateaux")
public class Bateau {
    @Id
    private String id;
    private String nom;
    private String type;
    private int capaciteMax;
    private String proprietaireNom;
    private double prixParHeure;
    private boolean disponible;
    private Date dateCreation;
}
