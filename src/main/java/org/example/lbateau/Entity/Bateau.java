package org.example.lbateau.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "bateaux")
public class Bateau {
    @Id
    private String id;
    private String nom;
    private String type;
    private String marque;
    private String internalId;
    private int capaciteMax;
    private String proprietaireNom;
    private String puissance;
    private String permis;
    private String description;
    private String statut;
    private String imageUrl;
    private double prixParHeure;
    private boolean disponible;
    private Date dateCreation;
}
