package com.recycling.controller;

import com.recycling.model.CollectionPoint;
import com.recycling.service.CollectionPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Contrôleur pour la gestion des points de collecte
 */
@RestController
@RequestMapping("/api/collection-points")
@RequiredArgsConstructor
public class CollectionPointController {

    private final CollectionPointService collectionPointService;

    /**
     * Créer un nouveau point de collecte (Admin)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CollectionPoint> createCollectionPoint(@Valid @RequestBody CollectionPoint collectionPoint) {
        CollectionPoint createdPoint = collectionPointService.createCollectionPoint(collectionPoint);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPoint);
    }

    /**
     * Récupérer tous les points de collecte actifs
     */
    @GetMapping("/public")
    public ResponseEntity<List<CollectionPoint>> getAllCollectionPoints() {
        List<CollectionPoint> points = collectionPointService.getAllActiveCollectionPoints();
        return ResponseEntity.ok(points);
    }

    /**
     * Récupérer un point de collecte par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CollectionPoint> getCollectionPointById(@PathVariable Long id) {
        CollectionPoint point = collectionPointService.getCollectionPointById(id)
                .orElseThrow(() -> new RuntimeException("Point de collecte non trouvé"));
        return ResponseEntity.ok(point);
    }

    /**
     * Rechercher des points de collecte
     */
    @GetMapping("/search")
    public ResponseEntity<List<CollectionPoint>> searchCollectionPoints(@RequestParam String query) {
        List<CollectionPoint> points = collectionPointService.searchCollectionPoints(query);
        return ResponseEntity.ok(points);
    }

    /**
     * Récupérer les points de collecte d'une ville
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<CollectionPoint>> getCollectionPointsByCity(@PathVariable String city) {
        List<CollectionPoint> points = collectionPointService.getCollectionPointsByCity(city);
        return ResponseEntity.ok(points);
    }

    /**
     * Récupérer les points de collecte presque pleins
     */
    @GetMapping("/nearly-full")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<CollectionPoint>> getNearlyFullCollectionPoints() {
        List<CollectionPoint> points = collectionPointService.getNearlyFullCollectionPoints();
        return ResponseEntity.ok(points);
    }

    /**
     * Mettre à jour un point de collecte (Admin)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CollectionPoint> updateCollectionPoint(@PathVariable Long id, 
                                                                  @Valid @RequestBody CollectionPoint collectionPoint) {
        collectionPoint.setId(id);
        CollectionPoint updatedPoint = collectionPointService.updateCollectionPoint(collectionPoint);
        return ResponseEntity.ok(updatedPoint);
    }

    /**
     * Ajouter un déchet à un point de collecte (Admin)
     */
    @PostMapping("/{pointId}/waste/{wasteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addWasteToCollectionPoint(@PathVariable Long pointId, @PathVariable Long wasteId) {
        collectionPointService.addWasteToCollectionPoint(pointId, wasteId);
        return ResponseEntity.ok(new java.util.HashMap<String, String>()
            .put("message", "Déchet ajouté au point de collecte")
        );
    }

    /**
     * Retirer un déchet d'un point de collecte (Admin)
     */
    @DeleteMapping("/{pointId}/waste/{wasteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeWasteFromCollectionPoint(@PathVariable Long pointId, @PathVariable Long wasteId) {
        collectionPointService.removeWasteFromCollectionPoint(pointId, wasteId);
        return ResponseEntity.ok(new java.util.HashMap<String, String>()
            .put("message", "Déchet retiré du point de collecte")
        );
    }

    /**
     * Supprimer un point de collecte (Admin)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCollectionPoint(@PathVariable Long id) {
        collectionPointService.deleteCollectionPoint(id);
        return ResponseEntity.ok(new java.util.HashMap<String, String>()
            .put("message", "Point de collecte supprimé")
        );
    }

    /**
     * Compter les points de collecte actifs
     */
    @GetMapping("/stats/active-count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getActiveCollectionPointsCount() {
        long count = collectionPointService.getActiveCollectionPointsCount();
        return ResponseEntity.ok(new java.util.HashMap<String, Long>()
            .put("activeCount", count)
        );
    }
}