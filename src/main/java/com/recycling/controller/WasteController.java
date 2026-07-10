package com.recycling.controller;

import com.recycling.model.Waste;
import com.recycling.service.WasteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Contrôleur pour la gestion des déchets
 */
@RestController
@RequestMapping("/api/waste")
@RequiredArgsConstructor
public class WasteController {

    private final WasteService wasteService;

    /**
     * Créer un nouveau déchet (Admin)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Waste> createWaste(@Valid @RequestBody Waste waste) {
        Waste createdWaste = wasteService.createWaste(waste);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWaste);
    }

    /**
     * Récupérer tous les déchets actifs
     */
    @GetMapping
    public ResponseEntity<List<Waste>> getAllWaste() {
        List<Waste> wastes = wasteService.getAllActiveWaste();
        return ResponseEntity.ok(wastes);
    }

    /**
     * Récupérer un déchet par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Waste> getWasteById(@PathVariable Long id) {
        Waste waste = wasteService.getWasteById(id)
                .orElseThrow(() -> new RuntimeException("Déchet non trouvé"));
        return ResponseEntity.ok(waste);
    }

    /**
     * Rechercher des déchets
     */
    @GetMapping("/search")
    public ResponseEntity<List<Waste>> searchWaste(@RequestParam String query) {
        List<Waste> wastes = wasteService.searchWaste(query);
        return ResponseEntity.ok(wastes);
    }

    /**
     * Récupérer les déchets recyclables
     */
    @GetMapping("/recyclable")
    public ResponseEntity<List<Waste>> getRecyclableWaste() {
        List<Waste> wastes = wasteService.getRecyclableWaste();
        return ResponseEntity.ok(wastes);
    }

    /**
     * Récupérer les déchets dangereux
     */
    @GetMapping("/hazardous")
    public ResponseEntity<List<Waste>> getHazardousWaste() {
        List<Waste> wastes = wasteService.getHazardousWaste();
        return ResponseEntity.ok(wastes);
    }

    /**
     * Récupérer les déchets les plus recherchés
     */
    @GetMapping("/popular")
    public ResponseEntity<List<Waste>> getMostSearchedWaste() {
        List<Waste> wastes = wasteService.getMostSearchedWaste();
        return ResponseEntity.ok(wastes);
    }

    /**
     * Mettre à jour un déchet (Admin)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Waste> updateWaste(@PathVariable Long id, @Valid @RequestBody Waste waste) {
        waste.setId(id);
        Waste updatedWaste = wasteService.updateWaste(waste);
        return ResponseEntity.ok(updatedWaste);
    }

    /**
     * Supprimer un déchet (Admin)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteWaste(@PathVariable Long id) {
        wasteService.deleteWaste(id);
        return ResponseEntity.ok(new java.util.HashMap<String, String>()
            .put("message", "Déchet supprimé")
        );
    }

    /**
     * Récupérer les déchets par catégorie
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Waste>> getWasteByCategory(@PathVariable String category) {
        List<Waste> wastes = wasteService.getWasteByCategory(category);
        return ResponseEntity.ok(wastes);
    }
}