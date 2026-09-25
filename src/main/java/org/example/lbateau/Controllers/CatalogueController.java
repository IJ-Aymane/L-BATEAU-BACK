package org.example.lbateau.Controllers;

import org.example.lbateau.Entity.Bateau;
import org.example.lbateau.Services.BateauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {

    @Autowired
    private BateauService bateauService;

    @GetMapping
    public List<Bateau> getCatalogue() {
        return bateauService.getAllBateaux();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bateau> getCatalogueItem(@PathVariable String id) {
        return bateauService.getBateauById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
