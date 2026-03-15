package org.example.lbateau.Controllers;

import org.example.lbateau.Entity.Bateau;
import org.example.lbateau.Services.BateauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bateaux")
@CrossOrigin("*")
public class BateauController {

    @Autowired
    private BateauService bateauService; // Daba kankhdmo b Service

    @GetMapping
    public List<Bateau> getAllBateaux() {
        return bateauService.getAllBateaux();
    }

    @GetMapping("/{id}")
    public Optional<Bateau> getBateauById(@PathVariable String id) {
        return bateauService.getBateauById(id);
    }

    @PostMapping
    public Bateau createBateau(@RequestBody Bateau bateau) {
        return bateauService.createBateau(bateau);
    }

    @PutMapping("/{id}")
    public Bateau updateBateau(@PathVariable String id, @RequestBody Bateau bateau) {
        return bateauService.updateBateau(id, bateau);
    }

    @DeleteMapping("/{id}")
    public void deleteBateau(@PathVariable String id) {
        bateauService.deleteBateau(id);
    }
}