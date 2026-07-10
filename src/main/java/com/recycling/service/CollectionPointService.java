package com.recycling.service;

import com.recycling.model.CollectionPoint;
import com.recycling.model.Waste;
import com.recycling.repository.CollectionPointRepository;
import com.recycling.repository.WasteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des points de collecte
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CollectionPointService {

    private final CollectionPointRepository collectionPointRepository;
    private final WasteRepository wasteRepository;
    private final ActivityLogService activityLogService;

    /**
     * Créer un nouveau point de collecte
     */
    public CollectionPoint createCollectionPoint(CollectionPoint collectionPoint) {
        CollectionPoint savedPoint = collectionPointRepository.save(collectionPoint);
        
        activityLogService.logActivity(null, "SYSTEM", 
            ActivityLogService.ActionType.CREATE, "CollectionPoint", savedPoint.getId(), 
            "Création d'un nouveau point de collecte: " + savedPoint.getName(), null, null);
        
        return savedPoint;
    }

    /**
     * Récupérer un point de collecte par ID
     */
    public Optional<CollectionPoint> getCollectionPointById(Long id) {
        return collectionPointRepository.findById(id);
    }

    /**
     * Mettre à jour un point de collecte
     */
    public CollectionPoint updateCollectionPoint(CollectionPoint collectionPoint) {
        CollectionPoint existingPoint = collectionPointRepository.findById(collectionPoint.getId())
            .orElseThrow(() -> new RuntimeException("Point de collecte non trouvé"));
        
        existingPoint.setName(collectionPoint.getName());
        existingPoint.setAddress(collectionPoint.getAddress());
        existingPoint.setCity(collectionPoint.getCity());
        existingPoint.setPostalCode(collectionPoint.getPostalCode());
        existingPoint.setLatitude(collectionPoint.getLatitude());
        existingPoint.setLongitude(collectionPoint.getLongitude());
        existingPoint.setPhoneNumber(collectionPoint.getPhoneNumber());
        existingPoint.setDescription(collectionPoint.getDescription());
        existingPoint.setOperatingHours(collectionPoint.getOperatingHours());
        existingPoint.setCapacityKg(collectionPoint.getCapacityKg());
        existingPoint.setCurrentUsageKg(collectionPoint.getCurrentUsageKg());
        existingPoint.setHasParking(collectionPoint.isHasParking());
        existingPoint.setWheelchairAccessible(collectionPoint.isWheelchairAccessible());
        existingPoint.setImageUrl(collectionPoint.getImageUrl());
        
        CollectionPoint updatedPoint = collectionPointRepository.save(existingPoint);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "CollectionPoint", collectionPoint.getId(), 
            "Modification du point de collecte: " + collectionPoint.getName(), null, null);
        
        return updatedPoint;
    }

    /**
     * Supprimer un point de collecte
     */
    public void deleteCollectionPoint(Long pointId) {
        CollectionPoint point = collectionPointRepository.findById(pointId)
            .orElseThrow(() -> new RuntimeException("Point de collecte non trouvé"));
        
        collectionPointRepository.delete(point);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.DELETE, "CollectionPoint", pointId, 
            "Suppression du point de collecte: " + point.getName(), null, null);
    }

    /**
     * Ajouter un déchet accepté au point de collecte
     */
    public void addWasteToCollectionPoint(Long pointId, Long wasteId) {
        CollectionPoint point = collectionPointRepository.findById(pointId)
            .orElseThrow(() -> new RuntimeException("Point de collecte non trouvé"));
        Waste waste = wasteRepository.findById(wasteId)
            .orElseThrow(() -> new RuntimeException("Déchet non trouvé"));
        
        point.getAcceptedWaste().add(waste);
        collectionPointRepository.save(point);
    }

    /**
     * Retirer un déchet du point de collecte
     */
    public void removeWasteFromCollectionPoint(Long pointId, Long wasteId) {
        CollectionPoint point = collectionPointRepository.findById(pointId)
            .orElseThrow(() -> new RuntimeException("Point de collecte non trouvé"));
        
        point.getAcceptedWaste().removeIf(w -> w.getId().equals(wasteId));
        collectionPointRepository.save(point);
    }

    /**
     * Lister tous les points de collecte actifs
     */
    public List<CollectionPoint> getAllActiveCollectionPoints() {
        return collectionPointRepository.findAllActive();
    }

    /**
     * Lister tous les points de collecte
     */
    public List<CollectionPoint> getAllCollectionPoints() {
        return collectionPointRepository.findAll();
    }

    /**
     * Récupérer les points de collecte d'une ville
     */
    public List<CollectionPoint> getCollectionPointsByCity(String city) {
        return collectionPointRepository.findByCity(city);
    }

    /**
     * Récupérer le nombre de points de collecte actifs
     */
    public long getActiveCollectionPointsCount() {
        return collectionPointRepository.countActiveCollectionPoints();
    }

    /**
     * Rechercher des points de collecte
     */
    public List<CollectionPoint> searchCollectionPoints(String search) {
        return collectionPointRepository.searchCollectionPoints(search);
    }

    /**
     * Récupérer les points de collecte presque pleins
     */
    public List<CollectionPoint> getNearlyFullCollectionPoints() {
        return collectionPointRepository.findNearlyFullCollectionPoints();
    }
}