package org.example.lbateau.Services;

import org.example.lbateau.Entity.Bateau;
import org.example.lbateau.Repository.BateauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BateauService {

    @Autowired
    private BateauRepository bateauRepository;

    public List<Bateau> getAllBateaux() {
        return bateauRepository.findAll();
    }

    public Optional<Bateau> getBateauById(String id) {
        return bateauRepository.findById(id);
    }

    public Bateau createBateau(Bateau bateau) {
        bateau.setDateCreation(new Date());
        if (bateau.getStatut() == null || bateau.getStatut().isBlank()) {
            bateau.setStatut(bateau.isDisponible() ? "ACTIVE" : "HORS_SERVICE");
        }
        return bateauRepository.save(bateau);
    }

    public Bateau updateBateau(String id, Bateau bateauDetails) {
        Optional<Bateau> bateauExistant = bateauRepository.findById(id);

        if (bateauExistant.isPresent()) {
            Bateau bateau = bateauExistant.get();
            bateau.setNom(bateauDetails.getNom());
            bateau.setType(bateauDetails.getType());
            bateau.setMarque(bateauDetails.getMarque());
            bateau.setInternalId(bateauDetails.getInternalId());
            bateau.setCapaciteMax(bateauDetails.getCapaciteMax());
            bateau.setProprietaireNom(bateauDetails.getProprietaireNom());
            bateau.setPuissance(bateauDetails.getPuissance());
            bateau.setPermis(bateauDetails.getPermis());
            bateau.setDescription(bateauDetails.getDescription());
            bateau.setStatut(bateauDetails.getStatut());
            bateau.setImageUrl(bateauDetails.getImageUrl());
            bateau.setPrixParHeure(bateauDetails.getPrixParHeure());
            bateau.setDisponible(bateauDetails.isDisponible());

            if (bateau.getStatut() == null || bateau.getStatut().isBlank()) {
                bateau.setStatut(bateau.isDisponible() ? "ACTIVE" : "HORS_SERVICE");
            }

            return bateauRepository.save(bateau);
        }
        return null;
    }

    public void deleteBateau(String id) {
        bateauRepository.deleteById(id);
    }
}
