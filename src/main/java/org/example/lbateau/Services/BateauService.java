package org.example.lbateau.Services;


import org.example.lbateau.Entity.Bateau;
import org.example.lbateau.Repository.BateauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service // Katgoul l Spring bli hadi hiya l'couche Service
public class BateauService {

    @Autowired
    private BateauRepository bateauRepository;

    // Jbed ga3 les bateaux
    public List<Bateau> getAllBateaux() {
        return bateauRepository.findAll();
    }

    // Jbed bateau wa7d b l'ID dialo
    public Optional<Bateau> getBateauById(String id) {
        return bateauRepository.findById(id);
    }

    // Zid bateau jdid
    public Bateau createBateau(Bateau bateau) {
        bateau.setDateCreation(new Date());
        return bateauRepository.save(bateau);
    }

    // Modifier bateau kayn deja
    public Bateau updateBateau(String id, Bateau bateauDetails) {
        Optional<Bateau> bateauExistant = bateauRepository.findById(id);

        if (bateauExistant.isPresent()) {
            Bateau bateau = bateauExistant.get();
            // Kanbdlo ghir l'ma3loumat li tbedlo
            bateau.setNom(bateauDetails.getNom());
            bateau.setType(bateauDetails.getType());
            bateau.setCapaciteMax(bateauDetails.getCapaciteMax());
            bateau.setProprietaireNom(bateauDetails.getProprietaireNom());
            bateau.setPrixParHeure(bateauDetails.getPrixParHeure());
            bateau.setDisponible(bateauDetails.isDisponible());

            return bateauRepository.save(bateau);
        }
        return null; // Awla t9der dir exception hna
    }

    // Mse7 bateau
    public void deleteBateau(String id) {
        bateauRepository.deleteById(id);
    }
}